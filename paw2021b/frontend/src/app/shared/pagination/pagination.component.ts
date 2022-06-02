import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnInit {

  @Input() pages: number;
  @Input() current: number;
  @Output() changePage = new EventEmitter<number>();

  constructor() { }

  ngOnInit(): void {
  }

  setPage(page: number){
    this.changePage.emit(page);
  }

  pageSeq(n: number): Array<number>{
    return Array(n).fill(1).map((x,i) => i);
  }

}
