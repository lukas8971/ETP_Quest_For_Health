export class CompletedQuest {
  constructor(
    public quest: number,
    public user: number,
    public completedOn: Date,
    public completed: boolean ) {}
}
