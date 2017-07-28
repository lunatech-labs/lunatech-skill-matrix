(function () {
  'use strict';

  angular.module('techmatrix').constant('operations', {
      EQUAL:{
          text:"equal",
          symbol:"=",
          value:"EQUAL"
      },
      GT:{
          text:"greater than",
          symbol:">",
          value:"GT"
      },
      LT:{
          text:"lower than",
          symbol:"<",
          value:"LT"
      },
      ANY:{
        text:"Any",
        symbol:"_",
        value:"ANY"
      }
  });
})()