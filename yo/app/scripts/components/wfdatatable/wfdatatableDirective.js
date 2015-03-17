define(['angular','datatables'],function(angular,datatables) {
'use strict';

angular.module('wfdatatable.wfdatatableDirective',[])
.directive("wfThBtn",[WfThBtn])
.directive("wfTh",[WfTh])
.directive("wfThComplex",[WfThComplex])
.directive("wfThClick",[WfThClick])
.directive("wfDatatable",[WfDatatable]);

function WfThBtn() {
	return {
		require:"wfTh",
		restrict:"A",
		transclude:false,
		priority:900,
		link:function($scope,elm,attrs,ctrl) {
			var clickNames=attrs.wfThBtn;
			var tpl=attrs.wfThBtnTpl;
			ctrl.$renderCtrls.push(function ( ind,tdElm, data) {
				if(!tdElm.hasClass("wf-th-btn")) {
					tdElm.addClass("wf-th-btn");
					var clicks=clickNames.split(",");
					if(!tpl) {
						tpl="";
						$.each(clicks,function(key1,val1) {
							tpl+="<a>Click</a>";
						});
					}
					
					var links=$(tpl);
					tdElm.append(links);
					var btns=$("a,button",tdElm);
					console.dir(btns);
					$.each(btns,function(key1,val1){
						if(key1<clicks.length) {
							console.info("bind click");
							$(val1).click(function() {
								console.info("click "+key1);
								if($scope[clicks[key1]]) {
									$scope[clicks[key1]](ind,data);
								}
								
							});
						}
					});
					
					
				}
			});
		}
	}
};
function WfThClick() {
	return {
		require:'^WfThComplex',
		restrict:'EA',
		replace:true,
		transclude:true,
		priority:1000,
		link:function($scope,elm,attrs,ctrl) {
			console.info("link click");
			var id="wfControlId"+ctrl.$actionControls.length;
			ctrl.$actionControls[id]={
				"action":"click",
				"name":attrs.WfThClick
			};
			elm.addClass(id);
		}
	};
}
function WfThComplex() {

	var $actionControls={};
	return {
		require:'^wfDatatable',
		restrict:'EA',
		replace:true,
		transclude:true,
		priority:1000,
		controller:function() {
			this.$actionControls=$actionControls;
		},
		/*
		compile: function(tElement, tAttrs, transclude) {
			
      		return {
          		pre: function preLink(scope, iElement, iAttrs, controller) {
            		transclude({},function(clone) {
            			var content="";
            			$.each(clone,function(ind,node) {
            				if(node.nodeType==1) content+=node.outerHTML;
            			});
            			scope.myElement=content.trim();			
					});
            		//console.dir(iElement);
          		}
          	}
        },*/
		link:function($scope,elm,attrs,ctrl) {
			console.info("link complex");
			console.dir($actionControls);
        	var content = elm.children();
        	console.info(content);
      		
			console.dir(elm);
/*			var name=attrs.wfTh;
			var config={};
			if(!name) {
				config.data=null;
				config.defaultContent="";
			} else {
				config.data=name;
			}
			if($renderCtrls.length>0) {
				ctrl.$colRenders.push($renderCtrls[0]);
			} else {
				ctrl.$colRenders.push(null);
			}
			
			ctrl.$colOptions.push(config);
			*/
		}
	}

}

function WfTh() {
	var $renderCtrls=[];
	return {
		require:'^wfDatatable',
		restrict:'EA',
		transclude:false,
		priority:1000,
		controller:function() {
			this.$renderCtrls=$renderCtrls;
		},
		link:function($scope,elm,attrs,ctrl) {
			var name=attrs.wfTh;
			var config={};
			if(!name) {
				config.data=null;
				config.defaultContent="";
			} else {
				config.data=name;
			}
			if($renderCtrls.length>0) {
				ctrl.$colRenders.push($renderCtrls[0]);
			} else {
				ctrl.$colRenders.push(null);
			}
			
			ctrl.$colOptions.push(config);
		}
	}
};

function WfDatatable() {
	var $colOptions=[];
	var $colRenders=[];
	var dataTable=null;
	return {
      restrict:'EA',
      transclude:true,
      replace: true,
      scope:{
        wfData:'='
      },
      template:"<div ng-transclude></div>",
      controller: function() {
      	this.$colOptions=$colOptions;
      	this.$colRenders=$colRenders;
      },
      link: function($scope,elm,attrs) {
      	var option={};
      	angular.copy(defaultOption,option);
      	option.fnRowCallback=function( nRow, aData, iDisplayIndex) {
			var nTds=$(nRow).children();
			$.each($colRenders,function(ind,val) {
				if(val) {	
					val(ind,$(nTds[ind]),aData);
				}
			});
		};
      	option.columns=$colOptions;
      	dataTable=$("table",elm).dataTable(option);
      	$scope.$watch('wfData',function(newVal,oldVal) {
      		dataTable.fnAddData(newVal);
      	},true);

	  	//console.dir($colOptions);
	  }      
	};

var defaultOption={
			"sDom": "<'row-fluid'<'span6'f><'row-fluid'<'#"+id+"_btnGrp'>r>t<'row-fluid'<'span12'p>>",
			"sPaginationType": "bs_normal",
			//"sPaginationType": "bootstrap",
			"oLanguage": {
				"sSearch":"",
				"sLengthMenu": "_MENU_ per page"
			},
			
			"fnRowCallback": function( nRow, aData, iDisplayIndex) {
				var nTds=$(nRow).children();
				
				var logColIndex=6;
				var logTd=$(nTds[logColIndex]);
				if(aData[logColIndex]) {
				
					logTd.html(
					"<a href='"+aData[logColIndex]+"' target='_blank'>Cal Log</a>");
					
				} else {
					logTd.html("");
				}
				
				var actionColIndex=7;
				var actionTd=$(nTds[actionColIndex]);
				if($("a",actionTd).length==0) {
					var seriesId=self.getSeriesId(self.context.curSel.rank_cat,aData[2],aData[3],aData[4]);
					if(self.context.activeSeries[seriesId]) {
						aData[actionColIndex]="Hide";
					} else{
						aData[actionColIndex]="Chart";
					}
					
					var btnShowChart=$("<a>"+aData[actionColIndex]+"</a>");
					btnShowChart.click(function() {
						if(aData[actionColIndex]=="Chart") {
							self.addSerial(self.context.curSel.groupId,self.context.curSel.rank_cat,aData[2],aData[3],aData[4]);
							aData[actionColIndex]="Hide";
							
						} else {
							self.removeSeries(self.context.curSel.groupId,self.context.curSel.rank_cat,aData[2],aData[3],aData[4]);
							aData[actionColIndex]="Chart";
						}
						$(btnShowChart).text(aData[actionColIndex]);
						//console.info("click me");
					})
					actionTd.append(btnShowChart);					
				}
				
				return nRow;
			}
	};
	
}; //END OF WfDatatable



});