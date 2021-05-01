import {User} from "./user";
import {Quest} from "./quest";

export interface UserQuests
{
  user: User;
  quests: Quest[];
}
