import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Api } from '../../services/api';
import { Turma, Disciplina } from '../../models/models';

import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-selection',
  standalone: true,
  imports: [FormsModule, CommonModule,
      MatSelectModule,
      MatFormFieldModule,
      MatProgressSpinnerModule
  ],
  templateUrl: './selection.html',
  styleUrl: './selection.scss',
})
export class Selection implements OnInit {

    loading: boolean = false;
    errorMsg: string = '';

    turmas: Turma[] = [];
    disciplinas: Disciplina[] = [];
    turmaId?: number;
    disciplinaId?: number;
    @Output() selectionChanged = new EventEmitter<{turmaId:number, disciplinaId:number}>();

    constructor(private api: Api) {}

    ngOnInit() {
        this.api.getTurmas().subscribe(t => this.turmas = t);
        this.api.getDisciplinas().subscribe(d => this.disciplinas = d);

        /** 
        this.api.getTurmas().subscribe({
            next: t => { console.log('turmas loaded', t); this.turmas = t; },
            error: e => console.error('turmas error', e)
        });
        this.api.getDisciplinas().subscribe({
            next: d => { console.log('disciplinas loaded', d); this.disciplinas = d; },
            error: e => console.error('disciplinas error', e)
        });
        **/

        // optional: restore from sessionStorage
    }

    onChange() {
        if (this.turmaId != null && this.disciplinaId != null) {
            this.selectionChanged.emit({ turmaId: this.turmaId, disciplinaId: this.disciplinaId });
        }
    }

}
