import {Credentials} from "./credentials";
import {Quest} from "./quest";

export class CreateDoctorQuest{
  constructor(public credentials: Credentials, public quest: Quest){}
}
