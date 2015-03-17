define(['angular','bootstrap'],function(angular) {
'use strict';

/****BEGIN DEFINITION*****/
angular.module('wfscrollcontainer',['wffix'])
.directive('wfScrollContainer',['$timeout',WfScrollContainer]);

function WfScrollContainer($timeout) {
	return {
            restrict:'EA',
            template:
            		"<div class='my-transclude'></div>"
                        ,
            transclude:true,
            link: function($scope,elm,attrs) {
                  var navId=attrs["wfNav"];
                  //console.info(navId);
                 // console.dir(attrs);
                 // $("div.my-transclude",elm).scrollspy({ target: '#'+navId });      
                $("body").scrollspy({ target: '#'+navId });

                $('#'+navId+' a.page-scroll').bind('click', function(event) {
                    var $anchor = $(this);
                    $('html, body').stop().animate({
                        scrollTop: $($anchor.attr('href')).offset().top
                    }, 1500);
                    event.preventDefault();
                });

            }
      };


};

})