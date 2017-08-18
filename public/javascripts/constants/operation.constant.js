(function () {
  'use strict';

  angular.module('techmatrix').constant('operations', {
      EQUAL:{
          text:"equal",
          symbol:"=",
          value:"EQUAL"
      },
      GT:{
          text:"greater than or equal",
          symbol:">=",
          value:"GTE"
      },
      LT:{
          text:"lower than or equal",
          symbol:"<=",
          value:"LTE"
      },
      ANY:{
        text:"Any",
        symbol:"_",
        value:"ANY"
      }
  });
})()