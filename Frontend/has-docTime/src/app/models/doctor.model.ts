import { Department } from "./department.model";
import { TimeSlot } from "./timeSlot.model";
import { User } from "./user.model";

export type Doctor = {
  id? : number;
  user : User;
  qualification : string;
  casesSolved : number;
  available : boolean;
  department : Department;
  availableTimeSlots? : TimeSlot[]
}
