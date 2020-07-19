/**
 * Created by abiwax on 2017-08-20.
 */

/*For setting Event Model*/
import {TVStationModel} from "./tvstations";
import {CategoryModel} from "./categories";

export class EventModel {

  id?: string = "";
  title?: string = "";
  subscribers?: number = 0;
  player1fname?: string = "";
  player2fname?: string = "";
  player1lname?: string = "";
  player2lname?: string = "";
  categories?: [CategoryModel];
  now_showing?: boolean = false;
  expired?: boolean = false;
  subscribed?: boolean = false;

  constructor() {

  }

}
