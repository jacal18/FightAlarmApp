import {Pipe, PipeTransform} from '@angular/core';

/**
 * Generated class for the GroupByPipe pipe.
 *
 * See https://angular.io/api/core/Pipe for more info on Angular Pipes.
 */
@Pipe({
  name: 'groupBy',
})
export class GroupByPipe implements PipeTransform {
  /**
   * Takes an array and groups by a specific field
   */
  transform(value: Array<any>, field: string): Array<any> {
    const groupedObj = value.reduce((prev, cur) => {
      if (!prev[cur[field]]) {
        prev[cur[field]] = [cur];
      } else {
        prev[cur[field]].push(cur);
      }
      return prev;
    }, {});
    let grouped_object = Object.keys(groupedObj).map(key => ({key, value: groupedObj[key]}))

    return grouped_object;
  }
}
