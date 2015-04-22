(function() {

angular.module('thank.common',[])
	.filter('nl2br', ['$sce',nl2brFilter])
	.directive('autolinker', ['$timeout',AutoLinkerDirective])
  .directive('wfTextLower',['$parse',WfTextLower])
  .directive('wfLogDom',[WfLogDom])

function nl2brFilter($sce){
	return function(msg,is_xhtml) { 
	    var is_xhtml = is_xhtml || true;
	    var breakTag = (is_xhtml) ? '<br />' : '<br>';
	    var msg = (msg + '').replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1'+ breakTag +'$2');
	    return $sce.trustAsHtml(msg);
	 }
} //end nl2br filter



// directives

function AutoLinkerDirective($timeout) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        $timeout(function() {
          var eleHtml = element.html();

          if (eleHtml === '') {
            return false;
          }

          var text = Autolinker.link(eleHtml, {
            className: 'autolinker',
            newWindow: false
          });

          element.html(text);

          var autolinks = element[0].getElementsByClassName('autolinker');

          for (var i = 0; i < autolinks.length; i++) {
            angular.element(autolinks[i]).bind('click', function(e) {
              var href = e.target.href;
              console.log('autolinkClick, href: ' + href);

              if (href) {
                //window.open(href, '_system');
                window.open(href, '_blank');
              }

              e.preventDefault();
              return false;
            });
          }
        }, 0);
      }
    }
} //end of AutoLinkerDirective


function WfTextLower($parse) {
   return {
     require: 'ngModel',
     link: function(scope, element, attrs, modelCtrl) {
        var lower = function(inputValue) {
           if (inputValue === undefined) { inputValue = ''; }
           var ret=inputValue.toLowerCase();
           if(ret !== inputValue) {
              modelCtrl.$setViewValue(ret);
              modelCtrl.$render();
            }         
            return ret;
         }
         modelCtrl.$parsers.push(lower);
         lower($parse(attrs.ngModel)(scope)); // capitalize initial value
     }
   };
} //end of WfTextLower

function WfLogDom() {
   return {
      restrict:"A",
      link:function($scope,element,attrs) {
         console.info("Dom Created: "+attrs.wfLogDom);
      }
   };
} //end of WFLogDom


})();