import {Quest} from "../dto/quest";

export interface HistoryQuest {
  quest: Quest;
  acceptedOn?: Date;
  completedOn?: Date;
  completed?: boolean;
}
