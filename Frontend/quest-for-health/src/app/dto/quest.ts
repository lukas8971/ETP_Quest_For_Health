
export interface Quest {
  id: number;
  name: string;
  description: string;
  exp_reward: number;
  gold_reward: number;
  repetition_cycle: number;
  dueDate: Date;
}
