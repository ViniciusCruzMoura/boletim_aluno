export interface Turma { id: number; nome: string; }
export interface Disciplina { id: number; nome: string; }
export interface Aluno { id: number; nome: string; }
export interface Avaliacao { id: number; titulo: string; peso: number; }
export interface Lancamento { alunoId: number; avaliacaoId: number; nota: number; }
