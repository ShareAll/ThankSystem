define(['angular'],function(angular) {
'use strict';

/****BEGIN DEFINITION*****/
angular.module('wful',[])
.directive('wfUl',['$timeout',WfUl]);
      

function WfUl($timeout) {
      var colSize=3;
      var itemWidth=165;
      var itemHeight=131;
      function getStyle(index) {
            var x=(index%colSize)*100+"%";
            var y=(Math.floor(index/colSize))*100+"%";
            return "translate3d("+x+","+y+",0)";
            
      }
      function getItemStyle(index) {
            var x=(index%colSize)*100+"%";
            var y=(Math.floor(index/colSize))*100+"%";
            var translateStyle="translate3d("+x+","+y+",0)";
            var ret="";
            ret+="'transform':'"+translateStyle+"',";
            ret+="'-webkit-transform':'"+translateStyle+"',";
            ret+="'width':'"+itemWidth+"px',"
            ret+="'height':'"+itemHeight+"px',"
            
            return "{"+ret+"}";
      };
      function setItemStyle(liElm,index) {
           
            var x=(index%colSize)*100+"%";
            var y=(Math.floor(index/colSize))*100+"%";
            var translateStyle="translate3d("+x+","+y+",0)";
            
            liElm.css("width",itemWidth+"px");
            liElm.css("height",itemHeight+"px");
            liElm.css("-webkit-transform",translateStyle);
            liElm.css("transform",translateStyle);
      };

      function setContainerSize(ulElm,itemCount) {
            var width=colSize*itemWidth+1;
            var height=(Math.floor(itemCount/colSize)+1)*(itemHeight+1);
            ulElm.css("width",width);
            ulElm.css("height",height);
      }
      return {
            scope:{
                  wfData:'='
            },
            restrict:'C',
            link: function($scope,elm,attrs,ngRepeat) {
                  //$scope.setItemStyle=setItemStyle;
                  $scope.$watchCollection("wfData",function(newVal,oldVal) {
                        //set new size
                        setContainerSize(elm,newVal.length);

                        
                        //console.info(newVal.length+":"+angular.element("li",elm).length);
                        $.each(newVal,function(ind,val) {
                              val.style=getStyle(ind);
                              val.width=itemWidth;
                              val.height=itemHeight;
                        })
                       /* $.each(angular.element("li",elm),function(ind,liElm) {
                              setItemStyle(angular.element(liElm),ind);
                                    
                        });                              
                        */
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