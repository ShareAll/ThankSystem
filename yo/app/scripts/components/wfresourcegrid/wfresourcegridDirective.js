define(['angular','d3','tooltipster','wf_reference','components/wfresourcegrid/wfresourcegridService'], function (angular,d3,tooltipster,resourceService) {
  'use strict';

angular.module('wfresourcegrid.wfResourcegridDirective',
  ['wfresourcegrid.wfResourcegridService'])
.directive('wfResourcegridDirective',['$window','wfResourcegridService',WfResourcegridDirective])
.directive('wfResourcegridToolbarDirective',WfResourcegridToolbarDirective)
.directive('wfResourcegridNavDirective',WfResourcegridNavDirective)
;

function WfResourcegridNavDirective() {
  return {
    template:   
    "<md-sidenav class=\"md-sidenav-left md-whiteframe-z2\" md-component-id=\"leftMenu\" md-is-locked-open=\"$media(\'gt-md\')\">"
        +"<md-toolbar class=\"md-theme-light\">"
            +"<h1 class=\"md-toolbar-tools\"><span style=\"margin-right:50%;\">Steps<\/span>"
            +"<md-button hide-gt-md ng-click=\"closeLeftMenu()\" style=\"width:28px;height:28px;\"class=\"md-fab\" aria-label=\"Time\">"
                +"<i class=\"fa fa-arrow-circle-left\"><\/i>"
            +"<\/md-button>"
            +"<\/h1>"
        +"<\/md-toolbar>"
        +"<md-content>"
          +"<md-list>"
            +"<md-item ng-repeat=\"step in resourceGridSteps\">"
              +"<md-item-content>"
                +"<div class=\"md-tile-content\">"
                  +"<h3>"
                    +"<i class=\"fa fa-angle-right\" style=\"margin-right:10px\"><\/i>"
                    +"{{step.name}}"
                      +"<md-button ng-click=\"openStep(step)\" style=\"width:28px;height:28px;float:right\"class=\"md-fab\" aria-label=\"Time\">"
                        +"<i class=\"fa fa-hand-o-right\"><\/i>"
                    +"<\/md-button>  "
                  +"<\/h3>"
                  +"<p>"
                    +"{{step.notes}}"
                  +"<\/p>"
                +"<\/div>"
              +"<\/md-item-content>"
            +"<\/md-item>"
          +"<\/md-list>"
        +"<\/md-content>"
    +"<\/md-sidenav>",
    restrict:'EA',
    transclude:false 

  }
}
//END OF FUNC WfResourcegridNavDirective

function WfResourcegridToolbarDirective() {
  return {
    template:"<div layout=\"row\" layout-align=\"space-around center\" style=\"width:300px\">"
            +"<md-button ng-click=\"toggleNav()\" style=\"width:28px;height:28px;\"class=\"md-fab\" aria-label=\"Time\" hide-gt-md>"
                +"<i class=\"fa fa-arrow-circle-right\"><\/i>"
            +"<\/md-button>"
            +"<md-button ng-click=\"loadSteps()\" style=\"width:28px;height:28px\" class=\"md-fab\" aria-label=\"Time\">"
                +"<i class=\"fa fa-clock-o\"><\/i>"
            +"<\/md-button>"
            +"<md-checkbox ng-model=\"wfresourcegrid.context.showAsStack\" aria-label=\"Set to Compact\">"
                +"Compact View"
            +"<\/md-checkbox>"
            +"<\/div>",
      restrict:'EA',
      transclude:false 
  }
}
//END OF FUNC WfResourcegridToolbarDirective

function WfResourcegridDirective($window,wfResourcegridService) {
//FUNCTION CONTENT BEGIN
  var context={
      svgMinHeight:450,
      svgMinWidth:320,
      titleHeight:50,
      y_offset:-150,
      x_offset:50,
      boxWidth:32,
      boxHeight:150,
      boxPaddingLeft:15,
      boxPaddingTop:10,
      computePerRow:1,
      showAsStack:false
  };

//CREATE SVG
  var svg=null;
  var dataCache=null;
  
  function isHeader(d) {
    if(d.id.indexOf("TYPE")==0) return true;
    return false;
  }

  function calculateWidth(windowWidth) {
    //recheck size
    var width=windowWidth;
    context.computePerRow=Math.floor(parseInt(width)/(context.boxWidth+context.boxPaddingLeft))-2;  
    if(context.computePerRow<0) context.computePerRow=1;
    console.info(context.computePerRow);
  }

  //process Data in thee arrays
  //1. category title: catTitles
  //2. computes including compute and compute type header:computes
  function processData(data,justViewChange) {
      //compare the diff and set diff
      if(!justViewChange) {
        if(!dataCache) {
          $.each(data,function(ind,val) {
              val.diff={
                "type":"new"
              };
          });
        } else {
          var tmpMap={};
          $.each(dataCache,function(ind,val) {
              tmpMap[val.id]=val;
          });
          $.each(data,function(ind,val) {
              var origin=tmpMap[val.id];
              if(!origin) {
                val.diff={type:"new"};
              } else if(origin.value!=val.value) {
                val.diff={type:"resourceChange",delta:val.value-origin.value};
              } else if(origin.category!=val.category) {
                val.diff={type:"categoryChange"};
              } else if(origin.type!=val.type) {
                val.diff={type:"typeChange"};
              } else {
                val.diff=null;
              }
          });
          
        }
        dataCache=data;
        
      }

      var headerCount=0;
      function visitObject(map,fnMap) {
        var ret=[];
        $.each(map,function(ind,val) {
            ret.concat(fnMap(ind,val));
        });
        return ret;
      }
      function createHeader(typeName) {
          var header={
                      x:0,
                      y:0,
                      id:"TYPE:HEADER_"+headerCount,
                      value:0,
                      name:typeName
          };
          headerCount++;
          return header;
      }
      function groupArray(arr,fnGroup) {
        var ret={};
        $.each(arr,function(ind,val) {
            var groupId=fnGroup(val);

            if(!ret[groupId]) {
                ret[groupId]={
                    name:groupId,
                    values:[]
                };
            }
            ret[groupId].values.push(val);
        });
        return ret;
      }
      //group data by category
      var catMap=groupArray(dataCache,function(obj) {
         return obj.category;
      });
      var catGrp=[];
      $.each(catMap,function(ind,val) {
        catGrp.push(ind);
      });
      catGrp.sort();
      
      
      var yData=0;//context.y_offset;
      // cascade the data to tree and add type header
      var ret={
          catTitles:[],
          computes:[],
          height:0
      };
       var curRow=[];
      visitObject(catGrp,function(ind,catName) {
          var catObj=catMap[catName];
          ret.catTitles.push({
              id:catObj.name,
              name:catObj.name,
              y:yData,
              x:0 
          });
          yData+=context.titleHeight;
          var typeMap=groupArray(catObj.values,function(obj) {return obj.type;});
          
          visitObject(typeMap,function(ind,typeObj){
              var header=createHeader(typeObj.name);

              //add in curRow: showAs Stack and current row can hold extra one compute obj
              if(context.showAsStack && curRow.length<context.computePerRow-1) {
                  header.x=10+(curRow.length)*(context.boxWidth+context.boxPaddingLeft);
                  header.y=yData;
                  curRow.push(header);
                  ret.computes.push(header);
              } else {
                  if(context.showAsStack && curRow.length>=context.computePerRow-1){
                      yData+=context.boxHeight+context.boxPaddingTop;
                  }
                  curRow=[header];
                  ret.computes.push(header);
                  header.x=10;
                  header.y=yData;
              }
              $.each(typeObj.values,function(ind,computeObj) {
                  if( !context.showAsStack && curRow.length==0) {
                      header=createHeader(typeObj.name);
                      curRow.push(header);
                      ret.computes.push(header);
                      header.x=10;
                      header.y=yData;                        
                  }
                  computeObj.x=10+(curRow.length)*(context.boxWidth+context.boxPaddingLeft);
                  computeObj.y=yData;
                  ret.computes.push(computeObj);
                  curRow.push(computeObj);
                  if(curRow.length>=context.computePerRow) {
                      yData+=context.boxHeight+context.boxPaddingTop;
                      curRow=[];
                      
                  }
              }); //$.each_typeObj.values
              if(curRow.length>0 && !context.showAsStack) {
                  yData+=context.boxHeight+context.boxPaddingTop; 
                  curRow=[];
              }
        });//visitObject_typeMap
        //PREVENT FROM showAsStack case miss the line break
        if(curRow.length>0) {
            yData+=context.boxHeight+context.boxPaddingTop; 
            curRow=[];
        }
  
     });//visitObject_catMap
    //set min height if less than 600
    ret.height=yData+context.boxHeight+context.boxPaddingTop;
    if(ret.height<context.svgMinHeight) ret.height=context.svgMinHeight;
    return ret;
  }

  function setData(elm,gdata) {
      if($("svg",elm).size()>0) {
          svg=d3.select("svg",elm);
          
      } else {
        svg=d3.select(elm).append("svg");
      }
        svg.attr("width","100%")
            .attr("height", context.svgMinHeight)
            .style("overflow-x","auto")
            .style("overflow-y","auto")
            .append("g")
            .attr("transform", "translate(0,0)");


      $("svg",elm).attr("height",gdata.height);
      
      //draw category_type
      var cat=svg.selectAll("g.category_title").data(gdata.catTitles,function(d) {return d.id;})
      cat.enter()
        .append("g")
          .attr("class","category_title")
        .append("text").attr("class","category_title")
          .attr("font-size","20px")
          .attr("font-weight","900")
          .attr("x","0")
          .attr("y","40")
          .text(function(d) {return d.name;});

      cat.transition().duration(750).attr("transform",function(d){
            return "translate("+d.x+","+d.y+")";
      });
      

      cat.exit().remove();

      //draw computes
      var compute=svg.selectAll("g.compute").data(gdata.computes,function(d){return d.id});


      function activateTooltip() {
        var config={
              content : $('<h3 class="popover-title"><b>All Properties</b></h3><div class="popover-content"><i class="icon-spinner icon-spin icon-large"></i></div>'),
              functionBefore : function(origin, continueTooltip) {
                  continueTooltip();
                  if(origin.data("tip")=='ok') {  
                    return;
                  }
                  wfResourcegridService
                    .getDetail(origin.data("id"))
                    .then(successFunc);
                  function successFunc(response) {
                        var sDetail=[];
                        sDetail.push('<h3 class="popover-title"><b>All Properties</b></h3>');
                        sDetail.push('<div class="popover-content"><table class="table table-hover table-condensed"><tbody>');
                        $.each(response,function(key,value){
                          sDetail.push('<tr class="tooltiptr"><td><b>'+key+'</b></td><td class="value">' + value + '</td></tr>');
                        });     
                        
                        sDetail.push('</tbody></table></div>');
                        
                        // update our tooltip content with our returned data
                        // and cache it
                        origin.tooltipster('update', $(sDetail.join("\r\n"))).data('tip', 'ok');

                  }
                  

              },
              functionAfter : function(origin) {
                  //origin.attr("class", "tooltipster");
              },
              theme : ".tooltipster-shadow",
              maxWidth : 500,
              interactive : true,
              interactiveTolerance : 350,
              updateAnimation : false,
              trigger : "hover",
              delay: 500,
              position : "top",
              offsetX : "27",
              offsetY : "-10"

          };
          
          $('g.compute[tooltip!="yes"]').tooltipster(config);
          $('g.compute[tooltip!="yes"]').attr("tooltip","yes");
      }

      //enter phase
      var computeDiv=compute.enter()
          .append("g")
            .attr("class","compute")
            .attr("data-id",function(d) {return d.id;})
            .attr("data-tip",function(d) {
              if(isHeader(d)) return "ok";
              else return null;
            })
          //  .style("filter", "url(#drop-shadow)")
           

      svg.selectAll("g.compute").data(gdata.computes,function(d) {return d.id;})
          .transition().duration(750)
          .attr("transform",function(d) {return "translate("+d.x+","+d.y+")";})

     computeDiv.append("image")
        .attr("xlink:href", function(d) {
          if(isHeader(d)) return wf_reference.img.resourceGrid_rectangle_type;
          else if(d.diff) return wf_reference.img.resourceGrid_rectangle_highlight;
          else return wf_reference.img.resourceGrid_rectangle;
        })
        .attr("x", -13)
        .attr("y", -10)
        .attr("width", 52)
        .attr("height", 170);

     svg.selectAll("image").data(gdata.computes,function(d) {return d.id})
        .attr("xlink:href", function(d) {
          if(isHeader(d)) return wf_reference.img.resourceGrid_rectangle_type;
          else if(d.diff) return wf_reference.img.resourceGrid_rectangle_highlight;
          else return wf_reference.img.resourceGrid_rectangle;
        });

      computeDiv.append("rect")
            .attr("class","content")
            .attr("x",2)
            .attr("width", context.boxWidth-4)
            .attr("rx",5)
            .attr("ry",5);

      svg.selectAll("rect.content").data(gdata.computes,function(d) {return d.id;})
        .transition().duration(750)
        .attr("y",function(d){return (100-d.value)*(context.boxHeight-2)/100;})
        .attr("fill",function(d) {
             return "green";
        })
        .attr("height", function(d) { return d.value*(context.boxHeight-4)/100; });              

      computeDiv.append("image")
        .attr("xlink:href", function(d) {
            if(isHeader(d)) return null;
            if(d.cloud=='aws') return wf_reference.img.resourceGrid_cloud_aws;
            else if(d.cloud=='google') return  wf_reference.img.resourceGrid_cloud_google;
            else if(d.cloud=='ali') return wf_reference.img.resourceGrid_cloud_ali;
            else return null;

        })
        .attr("x", 3)
        .attr("y", 5)
        .attr("width", 24)
        .attr("height", 24);

      computeDiv.append("text")
            .attr("class",function(d) {
              if(isHeader(d)) {
                return "compute_label type_label";
              } else return "compute_label";
            })
            .attr("transform", "rotate(-90)")
            .attr("dx",12)
            .attr("dy",".35em")
            .attr("y",15)
            .attr("x",function(d) {
              if(isHeader(d)) {
                return -context.boxHeight/4*3;
              } else return -context.boxHeight
            });

      svg.selectAll("text.compute_label").data(gdata.computes,function(d){return d.id;})
          .transition().duration(750)
          .attr("fill",function(d) {
              if(isHeader(d)) return "black";
              if(d.diff && d.diff.delta) {
                if(d.diff.delta>0) {
                  return "red";
                } else return "Blue";
              } else return "Black";
          })
          .text(function(d) {
              var title="";
              if(d.name) {
                title=(d.name.length>15)?(d.name.substring(0,15)+"..."):d.name;
              }

              if(isHeader(d)) {
                return d.name;
              } else if(d.diff && d.diff.delta) {
                if(d.diff.delta>0) {
                   return d.value+"(+"+d.diff.delta+") %:"+title;  
                } else {
                   return d.value+"("+d.diff.delta+") %:"+title;  
                }
              }  {
                return d.value+"%:"+title;        
              }
          });

      compute.exit().transition().duration(750)
          .attr("transform","translate(0,0)")
          .remove();

      activateTooltip();
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
          calculateWidth($w.width());
          $scope.$watch('wfData',setResourcegridData,true);
          $scope.$watch('wfContext',setResourcegridContext,true);
          function setResourcegridData(newVal,oldVal) {
              context.showAsStack=$scope.isCompact;
              var tmpData=[];
              if(!newVal) newVal=[];
              var value=angular.copy(newVal);
              $.each(value,function(ind,val) {
                tmpData.push($.extend({},val));
              });
              var gData=processData(tmpData,false);
              setData(elm[0],gData);
              console.info('resourcegridData Change');
          };

          function setResourcegridContext(newVal,oldVal) {
            context.showAsStack=newVal;            
            var gData=processData(dataCache,true);
            setData(elm[0],gData);
             
          };

          $w.bind("resize",function() {
              if(!svg) return;
              var elmWidth=$(elm).css("width");
              var newWidth=$w.width();
              newWidth=(newWidth<context.svgMinWidth)?context.svgMinWidth:newWidth;
              if(elmWidth!=newWidth) {
                $(elm).css("width",newWidth);
                calculateWidth(newWidth);
                var gData=processData(dataCache,true);
                setData(elm[0],gData);
              }
          });

      }
   };


};
//END OF FUNC WfResourcegridDirective


});
