import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Turma, Disciplina, Aluno, Avaliacao, Lancamento } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class Api {
    base = 'http://localhost:8080/api/boletim';
    constructor(private http: HttpClient) {}
    getTurmas() { return this.http.get<Turma[]>(`${this.base}/turmas`); }
    getDisciplinas() { return this.http.get<Disciplina[]>(`${this.base}/disciplinas`); }
    getAlunos(turmaId: number) { return this.http.get<Aluno[]>(`${this.base}/turmas/${turmaId}/alunos`); }
    getAvaliacoes(turmaId: number, disciplinaId: number) {
        return this.http.get<Avaliacao[]>(`${this.base}/turmas/${turmaId}/disciplinas/${disciplinaId}/avaliacoes`);
    }
    postLancamentos(turmaId: number, disciplinaId: number, lancs: Lancamento[]) {
        return this.http.post(`${this.base}/turmas/${turmaId}/disciplinas/${disciplinaId}/lancamentos`, { notas: lancs });
    }
    getLancamentos(turmaId: number, disciplinaId: number) {
        return this.http.get<{ avaliacoes: Avaliacao[]; linhas: Array<{ alunoId: number; nome: string; notas: Record<string, number>; media: string; }> }>(
            `${this.base}/turmas/${turmaId}/disciplinas/${disciplinaId}/lancamentos`
        );
    }
}
