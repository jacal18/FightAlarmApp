/**
 * Created by abiwax on 2017-08-20.
 */

/*For setting User Model*/
export class UserModel {

  uid?: string = "";
  email?: string = "";
  emailVerified?: boolean = false;
  firstName?: string = "";
  lastName?: string = "";
  photoURL?: string = "";
  location?: string = "";
  role?: string = "";
  password?: string = "";
  joineddate?: number;

  constructor() {
  }

}
