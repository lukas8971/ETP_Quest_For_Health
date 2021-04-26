
export interface Quest {
  id: number;
  name: string;
  description: string;
  exp_reward: number;
  gold_reward: number;
  dueDate: Date;
  repetition_cycle: string;
  exp_penalty: number;
  gold_penalty: number;
  doctor: number;
}
