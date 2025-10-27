import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Selection } from './components/selection/selection';
import { Grade } from './components/grade/grade';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule, Selection, Grade,
      MatCardModule,
      MatIconModule,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
    protected readonly title = signal('frontend');
    selected?: { turmaId:number; disciplinaId:number };
    onSelection(event: { turmaId:number; disciplinaId:number }) { this.selected = event; }
}
