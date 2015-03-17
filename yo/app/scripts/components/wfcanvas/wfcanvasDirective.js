var globalModel=null;
define(['angular','d3'], function (angular,d3) {
  'use strict';

angular.module('wfcanvas.wfcanvasDirective',
  [])
.directive('wfcanvasDirective',['$window',WfCanvasDirective])
;

var context={
      editNodeList:[],
      svgMinHeight:450,
      svgMinWidth:320,
      model:null,
      hasCreationArea:true
};
var model={};
function WfCanvasDirective($window) {
  var svg=null;
//FUNCTION CONTENT BEGIN
 
  function getMaxHeight(data) {
    var ret=context.svgMinHeight;
    $.each(data.nodes,function(ind,val) {
      ret=(ret<val.y)?val.y:ret;
    });
    return ret;
  }
  var setCanvasModel=function(model,data) {
    //max Height
    var minHeight=context.svgMinHeight;
    var nodeMap={};
    $.each(data.nodes,function(ind,val) {
      nodeMap[val.id]=val;
      minHeight=(minHeight<(val.y+100))?(val.y+100):minHeight;
      val.fromLink=[];
      val.toLink=[];
    });
    //filter links;
    var links=[];
    var linkMap={};
    if(data.links) {
      $.each(data.links,function(ind,val) {
          if(val.source==val.target) return;
          if(!nodeMap[val.source] || !nodeMap[val.target]) return;
          val.id="l"+val.source+"-"+val.target;
          var rid="l"+val.target+"-"+val.source;
          if(linkMap[val.id] || linkMap[rid]) return;
          nodeMap[val.source].fromLink.push(val.id);
          nodeMap[val.target].toLink.push(val.id);
          linkMap[val.id]=val;
          linkMap[rid]=val;
          links.push(val)    
      });
    };
    
    model.minHeight=minHeight;
    model.nodeMap=nodeMap;
    model.nodes=data.nodes;
    model.links=links;
    model.linkMap=linkMap;
    //this.minHeight=minHeight;
    //this.nodeMap=nodeMap;
    //this.nodes=data.nodes;
    //this.links=data.links;

  }

  function drawData(elm,data,wfContext) {
      //data={nodes:[{id,name,x,y}],links:[{from,to}]};
      //create svg
      var svgGroup=null;
      if($("svg",elm).size()>0) {
          svg=d3.select("svg",elm);
          svgGroup=svg.select("g.global");
      } else {    
        svg=d3.select(elm).append("svg");

        svgGroup=svg.attr("width","100%")
          .attr("height", context.svgMinHeight)
          .style("overflow-x","auto")
          .style("overflow-y","auto")
          .append("g")
          .attr("class","global")
          .attr("transform", "translate(0,0)")
        //be able to pannable, zoomable
        var zoomListener = d3.behavior.zoom().scaleExtent([1,1]).on("zoom", function() {
            if (d3.event.defaultPrevented) return;
            if(wfContext) {
                wfContext.gAxis={x:d3.event.translate[0],y:d3.event.translate[1]};
            }
            svgGroup.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");      
        });
        svg.call(zoomListener);        
      }



      setCanvasModel(model,data);
      globalModel=model;
      //console.dir(model);
      context.model=model;

      $("svg",elm).attr("height",model.minHeight);
      
      //draw line first which can be override by node
      var linkSelects=svgGroup.selectAll("line.link").data(model.links,function(d) {return d.id;});
      linkSelects.enter().append("line")
        .attr("class","link")
        .attr("id",function(d) {return d.id;})
        .attr("x1",function(d) {return model.nodeMap[d.source].x;})
        .attr("y1",function(d) {return model.nodeMap[d.source].y;})
        .attr("x2",function(d) {return model.nodeMap[d.target].x;})
        .attr("y2",function(d) {return model.nodeMap[d.target].y;})
        .style("stroke",function(d) {
            return "red";
        })
        .style("stroke-width",function(d) {
            return "2";
        })

      //draw line text
      var linkTextGroup=svgGroup.selectAll("g.link").data(model.links,function(d) {return d.id;});
      var linkTextGroupEnter=linkTextGroup.enter().append("g")
        .attr("class","link")
        .attr("id",function(d) {return d.id;}); //for g

        linkTextGroupEnter.append("text")
        .attr("class","link")
        .attr("font-size","11px")
        .attr("fill","black")
        .attr("id",function(d) {return d.id;}) //for text
        .text(function(d) {
            var sq=(model.nodeMap[d.source].x-model.nodeMap[d.target].x)*(model.nodeMap[d.source].x-model.nodeMap[d.target].x)
            +(model.nodeMap[d.source].y-model.nodeMap[d.target].y)*(model.nodeMap[d.source].y-model.nodeMap[d.target].y);
            return Math.floor(Math.sqrt(sq));
        });
      
      linkTextGroup.exit().remove();

        $.each(model.links,function(ind,val) {
            realignLinkText(val);
        });

      function realignLinkText(d) {
          d3.select("g#"+d.id).attr("transform",function() {
              var x=(model.nodeMap[d.source].x+model.nodeMap[d.target].x)/2;
              var y=(model.nodeMap[d.source].y+model.nodeMap[d.target].y)/2;
              return "translate("+x+","+y+")";
          });
          d3.select("text#"+d.id)
          .attr("transform",function() {
               var deltaX=model.nodeMap[d.source].x-model.nodeMap[d.target].x;
               var deltaY=model.nodeMap[d.source].y-model.nodeMap[d.target].y;
               if(deltaX==0) {
                  return "rotate(-90)";
               } else {
                  var angle=(Math.atan(deltaY/deltaX)*180)/Math.PI;  
                  return "rotate("+angle+")";
               }   
          })
          .text(function(d) {
            var sq=(model.nodeMap[d.source].x-model.nodeMap[d.target].x)*(model.nodeMap[d.source].x-model.nodeMap[d.target].x)
            +(model.nodeMap[d.source].y-model.nodeMap[d.target].y)*(model.nodeMap[d.source].y-model.nodeMap[d.target].y);
            return Math.floor(Math.sqrt(sq));
          });
      }
      //draw data.nodes
      var nodeSelects=svgGroup.selectAll("g.node").data(model.nodes,function(d) {return d.id;});


      
      //attach drag behavior to node
      var drag = d3.behavior.drag().origin(function(d) { return d; })
        .on("dragstart", function() {
            d3.event.sourceEvent.stopPropagation(); // silence other listeners
        })
        .on("drag", function(d){
            d.x=d3.event.x;
            d.y=d3.event.y;
            d3.select(this)
              .attr("transform", "translate("+d.x+","+d.y+")");
           // console.info("from link for "+d.id);
           // console.dir(d.fromLink);
           // console.info("to link for "+d.id);
           // console.dir(d.toLink);

            $.each(d.fromLink,function(ind,val) {
              d3.select("line#"+val)
                .attr("x1",d.x)
                .attr("y1",d.y);
                var link=model.linkMap[val];
                
                
                realignLinkText(link);
            });
           $.each(d.toLink,function(ind,val) {
              d3.select("line#"+val)
                .attr("x2",d.x)
                .attr("y2",d.y);
                var link=model.linkMap[val];
                console.dir(link);
                realignLinkText(link);
            });
             
        });

      var nodeG=nodeSelects.enter()
        .append("g")
        .attr("class","node")
        .attr("transform", function(d) {return "translate("+d.x+","+d.y+")";})
        .call(drag);
      
      nodeSelects
        .attr("transform", function(d) {return "translate("+d.x+","+d.y+")";})
        .call(drag);

      nodeG.append("text")
        .attr("x","-15")
        .attr("y","-15")
        .text(function(d){return d.name;})

      nodeG.append("circle")
        .attr("r",7)
        
      
  
      nodeSelects.exit()
        .transition().duration(750)
        .attr("transform","translate(0,0)").remove();

      linkSelects.exit().remove();
      return model;
  }
  //Private Function End


//FUNCTION CONTENT END
  var $w = angular.element($window);

  return {
      restrict:'EA',
      transclude:false,
      scope:{
        wfData:'=',
        wfContext:'='
      },
      link: function($scope,elm,attrs) {
          console.dir($scope.wfData);
          $scope.$watch('wfData.changed',drawGraph,true);
          
          function drawGraph(newVal,oldVal) {
              var model=drawData(elm[0],$scope.wfData,$scope.wfContext);
              $scope.canvasModel=model;
          };

         

          $w.bind("resize",function() {
              if(!svg) return;
              var elmWidth=$(elm).css("width");
              var newWidth=$w.width();
              
          });

      }
   };


};
//END OF FUNC 




});
