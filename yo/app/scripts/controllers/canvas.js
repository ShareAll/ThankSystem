define(['angular'], function (angular) {
  'use strict';

  
angular.module('wftoolsApp.controllers.canvasCtrl', [])
    .factory('wfGraphService',['$http','$q','$timeout','apiBase',WfGraphService])
    .controller('CanvasCtrl',['$scope','wfGraphService', CanvasCtrl]);

var maxNode=0;
function CanvasCtrl($scope,wfGraphService) {
    $scope.wfCanvas_data={
        changed:0,
        nodes:[],
        links:[]
    };
    $scope.nodeCount=15;
    $scope.edgeCount=10;
    $scope.tsp_path=[];
    $scope.wfCanvas_context={
       gAxis:{x:0,y:0}
    };
    $scope.editNodeList=[];
    var nodeNameMap={};
    var nodeIdMap={};
    function getNodes() {
      var nodes=[];
      $.each($scope.wfCanvas_data.nodes,function(ind,val) {
        nodes.push({
          id:val.name,
          x:val.x,
          y:val.y
        });
      });
      return nodes;
    }
    $(window).on("keyup",function() {
        var ev=window.event;
        if(ev.keyCode==17) {
            if($scope.editNodeList.length==0) return;
            var start=$scope.editNodeList[0];
            $scope.editNodeList.push(start);
            for(var i=1;i<$scope.editNodeList.length;i++) {
                $scope.wfCanvas_data.links.push({
                  source:$scope.editNodeList[i-1],
                  target:$scope.editNodeList[i]
                });
            }

            $scope.wfCanvas_data.changed=1-$scope.wfCanvas_data.changed;            
            $scope.editNodeList=[];  
            $scope.$apply();
        }      
    });
    $scope.addNewNode1=function() {
           var ev=window.event;
           if(ev.ctrlKey) {
              var newId=(maxNode++);
              var newX=ev.layerX-$scope.wfCanvas_context.gAxis.x;
              var newY=ev.layerY-$scope.wfCanvas_context.gAxis.y;

              $scope.wfCanvas_data.nodes.push({
                  id:newId,
                  name:"node "+ newId,
                  x:newX,
                  y:newY
              });
              $scope.editNodeList.push(newId);
              nodeNameMap["node "+ newId]=newId;
              nodeIdMap[newId]="node "+ newId;
              $scope.wfCanvas_data.changed=1-$scope.wfCanvas_data.changed;
           }

    }
    
    $scope.drawTsp=function() {
      var tspRequest={
        nodes:getNodes(),
        path:$scope.tsp_path
      };
      wfGraphService.tsp(tspRequest).then(function(data) {
          var path=data.data.path;
          var links=[];
          for(var i=0;i<path.length-1;i++) {
             var fromId=nodeNameMap[path[i]];
             var toId=nodeNameMap[path[i+1]];
             links.push({
                source:fromId,
                target:toId
             });
          };
          $scope.pathCost=data.data.cost;
          $scope.wfCanvas_data.links=links;
          $scope.wfCanvas_data.changed=1-$scope.wfCanvas_data.changed;
          $scope.tsp_path=path;   
      });
      
    };
    $scope.findMst=function() {
      var mstRequest={
          nodes:getNodes()
      };

      wfGraphService.mst(mstRequest).then(function(data) {
          var links=[];
          $.each(data.data,function(ind,val) {
              links.push({
                source:nodeNameMap[val.source],
                target:nodeNameMap[val.target]
              });
          });
          
          $scope.wfCanvas_data.links=links;
          $scope.wfCanvas_data.changed=1-$scope.wfCanvas_data.changed;
      });
    };


    $scope.addNewNode=function() {
        var newId=(maxNode++);
        $scope.wfCanvas_data.nodes.push({
           id:newId,
           name:"node "+ newId,
           x:"30",
           y:"30"
        });
        nodeNameMap["node "+ newId]=newId;
        nodeIdMap[newId]="node "+ newId;
        $scope.wfCanvas_data.changed=1-$scope.wfCanvas_data.changed;
    };
    $scope.addEdge=function(newEdge) {
        if(!newEdge) {
          alert("Please enter newEdge");
          return;
        }
        var path=newEdge.split("-");
        if(path.length!=2 ) {
          alert("Please enter format from-to");
          return;
        }
        $scope.wfCanvas_data.links.push({
          source:path[0].trim(),
          target:path[1].trim()
        });
        $scope.wfCanvas_data.changed=1-$scope.wfCanvas_data.changed; 
    }

    $scope.pathSum=function() {
       var path=$scope.path;
       var ids=path.split(",");
       var request=[];
       $.each(ids,function(ind,val) {
         request.push(nodeIdMap[val]);
       });
      var tspRequest={
        nodes:getNodes(),
        path:request
      };
      wfGraphService.pathSum(tspRequest).then(function(data) {
          console.dir(data.data);
          $scope.pathCost=data.data;
      });
       
       
    };

    $scope.initNode=function() {
        maxNode=0;
        var nodes=[];  
        nodeNameMap=[];
        nodeIdMap=[];
        $scope.tsp_path=[];
        for(var i=0;i<$scope.nodeCount;i++) {
            var node={
                id: i,
                name:'node '+i,
                x:Math.random()*800+40,
                y:Math.random()*400+80
            };
            nodes.push(node);
            nodeNameMap[node.name]=node.id;
            nodeIdMap[node.id]=node.name;
            maxNode++;
        }
        $scope.wfCanvas_data={
            changed:1-$scope.wfCanvas_data.changed,
            nodes:nodes,
            links:[]
        };

    };
    $scope.initEdge=function() {
        var nodes=$scope.wfCanvas_data.nodes;
        var links=[];
        for(var i=0;i<$scope.edgeCount;i++) {
            var from=0,to=0;
            while(from==to) {
                from=Math.floor(Math.random()*nodes.length);
                to=Math.floor(Math.random()*nodes.length);
            }
            links.push({source:from,target:to});
        }
        $scope.wfCanvas_data={
            changed:1-$scope.wfCanvas_data.changed,
            nodes:$scope.wfCanvas_data.nodes,
            links:links
        };
    };




 //private function end 



};



//SERVICE DEFINITON
function WfGraphService($http,$q,$timeout,apiBase) {
  return {
      tsp:tsp,
      pathSum:pathSum,
      mst:mst
  };
 

  function tsp(tspRequest) {
    return $http.post(apiBase+"/graph/tsp", tspRequest);
  };
  function pathSum(tspRequest) {
    return $http.post(apiBase+"/graph/pathSum", tspRequest);
  };
  function mst(mstRequest) {
    return $http.post(apiBase+"/graph/mst", mstRequest);
  };
}// END WfGraphService

///END 
});
