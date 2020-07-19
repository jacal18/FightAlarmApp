import {Pipe, PipeTransform} from '@angular/core';

/**
 * Generated class for the GroupByPipe pipe.
 *
 * See https://angular.io/api/core/Pipe for more info on Angular Pipes.
 */
@Pipe({
  name: 'extractDate',
})
export class ExtractDatePipe implements PipeTransform {
  /**
   * Takes an array and groups by a specific field
   */

  static getMonth(number) {
    let month = new Array();
    month[0] = "Jan";
    month[1] = "Feb";
    month[2] = "Mar";
    month[3] = "Apr";
    month[4] = "May";
    month[5] = "Jun";
    month[6] = "Jul";
    month[7] = "Aug";
    month[8] = "Sep";
    month[9] = "Oct";
    month[10] = "Nov";
    month[11] = "Dec";
    return month[number];
  };

  static getCountdown(date, type) {
    // Get todays date and time
    let now = new Date().getTime();
    let distance = 0;
    // Find the distance between now and the count down date
    if (type === "countdown") {
      distance = date.getTime() - now;
    } else {
      distance = now - date.getTime();
    }

    // Time calculations for days, hours, minutes and seconds
    let days = Math.floor(distance / (1000 * 60 * 60 * 24));
    let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
    let seconds = Math.floor((distance % (1000 * 60)) / 1000);
    return {distance: distance, days: days, hours: hours, minutes: minutes, seconds: seconds};
  };

  static setCountdown(date, type) {
    let date_calc = ExtractDatePipe.getCountdown(date, type);
    if (date_calc.distance < 0) {
      return "EXPIRED";
    } else {
      if(type === "now"){
        return `${date_calc.hours.toString()}`;
      } else {
        return `${date_calc.days.toString()}`;
      }
    }
  };

  transform(value: string, field: any): string {
    let date = new Date(value);
    if (typeof field === "string" && field === "year") {
      return date.getFullYear().toString();
    } else if (typeof field === "string" && field === "month") {
      return ExtractDatePipe.getMonth(date.getMonth());
    } else if (typeof field === "string" && field === "day") {
      return date.getDate().toString();
    } else if (typeof field === "string" && field === "left") {
      return ExtractDatePipe.setCountdown(date, "countdown");
    } else if (typeof field === "string" && field === "now") {
      return ExtractDatePipe.setCountdown(date, "now");
    } else if (typeof field === "string" && field === "homenow") {
      let date_calc = {distance: 0, days: 0, hours: 0, minutes: 0, seconds: 0};
      setInterval( () => {
        date_calc = ExtractDatePipe.getCountdown(date, "difference");
        console.log(`${date_calc.days} : ${date_calc.hours} : ${date_calc.minutes} : ${date_calc.seconds}`)
        return `${date_calc.days} : ${date_calc.hours} : ${date_calc.minutes} : ${date_calc.seconds}`;
      },1000);

    } else if (typeof field === "object") {
      if (field.now_showing && field.now_showing === true) {
        setInterval(function () {
          let date_calc = ExtractDatePipe.getCountdown(date, "difference");
          // If the count down is over, write some text
          if (field.home && field.home === true) {
            console.log(`${date_calc.days} : ${date_calc.hours} : ${date_calc.minutes} : ${date_calc.seconds}`)
            return `${date_calc.days} : ${date_calc.hours} : ${date_calc.minutes} : ${date_calc.seconds}`;
          } else {
            return `${date_calc.days.toString()}`;
          }
        }, 1000);
      } else {
        return ExtractDatePipe.setCountdown(date, "countdown");
      }

    } else {
      return "";
    }
  }
}
