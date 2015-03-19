define(['angular'],function(angular) {
'use strict';

/****BEGIN DEFINITION*****/
angular.module('wful',[])
.directive('wfUl',['$timeout','$window',WfUl]);
      

function WfUl($timeout,$window) {
      var curData=[];
      var colSize=2;
      var itemWidth=165;
      var itemHeight=131;
      function setItemStyles(containerWidth) {
            containerWidth=parseInt(containerWidth);
            if(containerWidth<itemWidth*2) containerWidth=itemWidth*2;

            colSize=Math.floor(containerWidth/itemWidth);
            console.info(containerWidth+","+itemWidth+","+colSize);
            $.each(curData,function(ind,val) {
                  val.style=getStyle(ind);
                  val.width=itemWidth;
                  val.height=itemHeight;
            });
            console.dir(curData);
      }
      function getStyle(index) {
            var x=(index%colSize)*100+"%";
            var y=(Math.floor(index/colSize))*100+"%";
            return "translate3d("+x+","+y+",0)";
            
      }
  
      
      
      var $w=angular.element($window);
      return {
            scope:{
                  wfData:'='
            },
            restrict:'C',
            link: function($scope,elm,attrs,ngRepeat) {
                  var elmWidth=$(elm).css("width");
                  console.info(elmWidth);
                  $w.bind("resize",function() {
                        elmWidth=$(elm).css("width");
                        setItemStyles(elmWidth);
                        console.info(elmWidth);
                  });
                 // console.info($w.width());
                  
                  $scope.$watchCollection("wfData",function(newVal,oldVal) {
                        //set new size
                       // setContainerSize(elm,newVal.length);
                        curData=newVal;
                        setItemStyles(elmWidth);
                        
                        console.info("data Change");
                        
                  });
                  
                  console.info("link wful");
                 /* elm.css("list-style-type","none");
                  elm.css("padding",0);
                  elm.css("position","relative");
                  elm.css("border","1px solid black");
                  elm.css("overflow-y","auto");
                  elm.css("overflow-x","hidden");
                  elm.css("width","600px");*/
                  //console.dir(lis); 
                 
            }
  	};
};


});