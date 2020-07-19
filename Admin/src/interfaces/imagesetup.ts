
import {ImageModel} from "../models/image";

export interface ImageSetup {
  add: (image: ImageModel) => void;
}
