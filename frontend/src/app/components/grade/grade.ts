import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Api } from '../../services/api';
import { Aluno, Avaliacao, Lancamento } from '../../models/models';
import { CommonModule } from '@angular/common';

import { MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

interface LancamentoLocal { alunoId: number; avaliacaoId: number; nota: number | null; }

@Component({
    selector: 'app-grade',
    standalone: true,
    imports: [CommonModule,
        MatTableModule,
        MatCardModule,
        MatButtonModule,
        MatFormFieldModule,
        MatInputModule
    ],
    templateUrl: './grade.html',
    styleUrl: './grade.scss',
})
export class Grade implements OnChanges {
    displayedColumns: string[] = ['aluno'];

    @Input() turmaId?: number;
    @Input() disciplinaId?: number;

    alunos: Aluno[] = [];
    avaliacoes: Avaliacao[] = [];
    // mapa chave "alunoId_avaliacaoId" -> nota (null = sem nota)
    notas = new Map<string, number | null>();
    medias = new Map<number, string>(); // alunoId -> média formatada ou '-'
    loading = false;
    errorMsg = '';

    constructor(private api: Api) {}

    ngOnChanges(changes: SimpleChanges) {
        console.log('AQUIAQUIAQUI');
        if ((changes['turmaId'] || changes['disciplinaId']) && this.turmaId && this.disciplinaId) {
            this.loadGrid();
            this.api.getLancamentos(this.turmaId!, this.disciplinaId!).subscribe(resp => {
                this.avaliacoes = resp.avaliacoes;
                this.alunos = resp.linhas.map(l => ({ id: l.alunoId, nome: l.nome }));
                this.notas.clear();
                for (const linha of resp.linhas) {
                    for (const a of this.avaliacoes) {
                        const v = linha.notas?.[String(a.id)];
                        this.notas.set(this.key(linha.alunoId, a.id), v != null ? Number(v) : null);
                    }
                    this.medias.set(linha.alunoId, linha.media ?? '-');
                }
            });
        } else {
            // reset quando não houver seleção
            this.alunos = [];
            this.avaliacoes = [];
            this.notas.clear();
            this.medias.clear();
        }
    }

    private loadGrid() {
        this.loading = true;
        this.errorMsg = '';
        Promise.all([
            this.api.getAlunos(this.turmaId!).toPromise(),
            this.api.getAvaliacoes(this.turmaId!, this.disciplinaId!).toPromise()
        ]).then(([alunos, avaliacoes]) => {
            this.alunos = alunos!;
            this.avaliacoes = avaliacoes!;
            this.initNotas();
            this.loading = false;

            this.displayedColumns = ['aluno'];

        // Add columns for each avaliacao
        this.avaliacoes.forEach(a => {
            this.displayedColumns.push('avaliacao_' + a.id);
        });

        // Add 'media' at the end
        this.displayedColumns.push('media');
        }).catch(err => {
            this.errorMsg = 'Erro ao carregar dados';
            this.loading = false;
        });
    }

    private initNotas() {
        this.notas.clear();
        this.medias.clear();
        for (const aluno of this.alunos) {
            for (const a of this.avaliacoes) {
                this.notas.set(this.key(aluno.id, a.id), null);
            }
            this.medias.set(aluno.id, '-');
        }
    }

    key(alunoId: number, avaliacaoId: number) { return `${alunoId}_${avaliacaoId}`; }

    onNotaChange(alunoId: number, avaliacaoId: number, value: string) {
        // parse and clamp
        const parsed = value === '' ? null : Number(value);
        if (parsed == null || isNaN(parsed)) {
            this.notas.set(this.key(alunoId, avaliacaoId), null);
        } else {
            const clamped = Math.max(0, Math.min(10, parseFloat(parsed.toFixed(1))));
            this.notas.set(this.key(alunoId, avaliacaoId), clamped);
        }
        this.recalcularMedia(alunoId);
    }

    private recalcularMedia(alunoId: number) {
        let numerador = 0;
        let denominador = 0;
        for (const a of this.avaliacoes) {
            const nota = this.notas.get(this.key(alunoId, a.id));
            if (nota != null && !isNaN(nota)) {
                numerador += nota * a.peso;
                denominador += a.peso;
            }
        }
        if (denominador === 0) this.medias.set(alunoId, '-');
        else this.medias.set(alunoId, (numerador / denominador).toFixed(1));
    }

    validarAntesDeEnviar(): { ok: boolean; msg?: string } {
        for (const [k, v] of this.notas.entries()) {
            if (v != null) {
                if (isNaN(v) || v < 0 || v > 10) return { ok: false, msg: 'Existem notas fora do intervalo 0–10' };
            }
        }
        return { ok: true };
    }

    montarLancamentos(): Lancamento[] {
        const lancs: Lancamento[] = [];
        for (const aluno of this.alunos) {
            for (const a of this.avaliacoes) {
                const nota = this.notas.get(this.key(aluno.id, a.id));
                if (nota != null && !isNaN(nota)) {
                    lancs.push({ alunoId: aluno.id, avaliacaoId: a.id, nota }); // nota is number here
                }
            }
        }
        return lancs;
    }

    salvar() {
        if (!this.turmaId || !this.disciplinaId) return;
        const v = this.validarAntesDeEnviar();
        if (!v.ok) { this.errorMsg = v.msg!; return; }

        const payload = this.montarLancamentos();
        if (payload.length === 0) { this.errorMsg = 'Nenhuma nota para salvar'; return; }

        this.loading = true;
        this.api.postLancamentos(this.turmaId, this.disciplinaId, payload).subscribe({
            next: () => { this.loading = false; this.errorMsg = ''; /*alert('Lançamentos salvos com sucesso');*/ },
                error: () => { this.loading = false; this.errorMsg = 'Erro ao salvar lançamentos'; }
        });
    }
}
