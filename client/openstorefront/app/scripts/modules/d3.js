/* 
* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
*
* Licensed under the Apache License, Version 2.0 (the 'License');
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an 'AS IS' BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
'use strict';

angular.module('d3', [])
.factory('d3Service', ['$document', '$window', '$q', '$rootScope', function($document, $window, $q, $rootScope) {
  var d = $q.defer(),
  d3service = {
    d3: function() { return d.promise; }
  };
  function onScriptLoad() {
    // Load client in the browser
    $rootScope.$apply(function() { d.resolve($window.d3); });
  }
  function loadTip(){
    scriptTag2.type = 'text/javascript'; 
    scriptTag2.charset = 'utf-8';
    scriptTag2.async = true;
    scriptTag2.src = 'scripts/common/d3tip.js';
    scriptTag2.onreadystatechange = function () {
      if (this.readyState == 'complete') onScriptLoad();
    }
    scriptTag2.onload = onScriptLoad;
  }
  var scriptTag2 = $document[0].createElement('script');
  var scriptTag = $document[0].createElement('script');
  scriptTag.type = 'text/javascript'; 
  scriptTag.charset = 'utf-8';
  scriptTag.async = true;
  scriptTag.src = 'scripts/common/d3.js';
  scriptTag.onreadystatechange = function () {
    if (this.readyState == 'complete') loadTip();
  }
  scriptTag.onload = loadTip;

  var s = $document[0].getElementsByTagName('body')[0];
  s.appendChild(scriptTag);
  s.appendChild(scriptTag2);

  return d3service;
}])
.config(['$provide', function($provide) {

  var customDecorator = function($delegate) {
    var d3Service = $delegate;
    d3Service.d3().then(function(d3) {
      // build our custom functions on the d3
      // object here
    });

    return d3Service; // important to return the service
  };

  $provide.decorator('d3Service', customDecorator);
}])
.directive('d3FirstGraph', ['$window', '$timeout', 'd3Service', function($window, $timeout, d3Service) {
  return {
    restrict: 'EA',
    scope: {
      owner: '=',
      data: '=',
      label: '@',
      readonly: '@',
      onClick: '&'
    },
    link: function(scope, ele, attrs) {
      // console.log('scope', scope);
      console.log('scope', angular.copy(scope.data));
      
      var checkTrue = function(value){
        if (value === 'true' || value === true || value === 1 || value === '1')
          return true;
        return false;
      }
      scope.nodes = [];
      scope.links = [];
      scope.owner = scope.owner || {};
      scope.owner.name = scope.owner.name || '';
      scope.owner.componentId = scope.owner.componentId || 0;

      (function(data){
        var tNodes = [];
        var tLinks = [];
        _.each(data, function(item){
          var source = _.find(tNodes, {'id': item.ownerComponentId});
          var target = _.find(tNodes, {'id': item.targetComponentId});
          if (!source) {
            source = {
              'id': item.ownerComponentId,
              'reflexive': false,
              'label': item.ownerComponentName
            }
            tNodes.push(source);
          }
          if (!target){
            target = {
              'id': item.targetComponentId,
              'reflexive': false,
              'label': item.targetComponentName
            }
            tNodes.push(target); 
          }
          if (source && target){

            tLinks.push({
              'source': source,
              'target': target,
              'left': false,
              'right': true,
              'label': item.relationshipTypeDescription,
              'type': item.relationshipType,
              'relId': item.relationshipId
            })
          }
        })
        // console.log('scope', scope);
        scope.nodes = tNodes; //
        scope.links = tLinks;
        scope.nodes = scope.nodes ||  [
        {id: 0, reflexive: false, label: 'this is cool'},
        {id: 1, reflexive: true , label: 'this is cool2'},
        {id: 2, reflexive: false, label: 'this is cool3'}
        ],
        scope.links = scope.links || [
        {source: scope.nodes[0], target: scope.nodes[1], left: false, right: true, label: 'relates to' },
        {source: scope.nodes[1], target: scope.nodes[2], left: false, right: true, label: 'depends on' },
        {source: scope.nodes[2], target: scope.nodes[1], left: false, right: true, label: 'Integrates with' }
        ];
      })(scope.data)
      // scope.nodes = [
      // {id: 0, reflexive: false, label: 'this is cool'},
      // {id: 1, reflexive: true , label: 'this is cool2'},
      // {id: 2, reflexive: false, label: 'this is cool3'}
      // ],
      // scope.links = [
      // {source: scope.nodes[0], target: scope.nodes[1], left: false, right: true, label: 'relates to' },
      // {source: scope.nodes[1], target: scope.nodes[2], left: false, right: true, label: 'depends on' },
      // {source: scope.nodes[2], target: scope.nodes[1], left: false, right: true, label: 'Integrates with' }
      // ];

      d3Service.d3().then(function(d3) {
        var renderTimeout;
        var margin = parseInt(attrs.margin) || 20,
        ballRadius = parseInt(attrs.ballRadius) || 5;

        var svg = d3.select(ele[0])
        .append('svg:svg')
        .style('width', '100%');
        console.log('svg', svg);
        
        svg[0][0].setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/svg");
        svg[0][0].setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xlink", "http://www.w3.org/1999/xlink");

        $window.onresize = function() {
          scope.$apply();
        };

        scope.$watch(function() {
          return angular.element($window)[0].innerWidth;
        }, function() {
          scope.render(scope.data);
        });

        scope.$watch('data', function(newData) {
          scope.render(newData);
        }, true);

        scope.render = function(data) {
          svg.selectAll('*').remove();
          if (!data) return;
          if (renderTimeout) clearTimeout(renderTimeout);

          renderTimeout = setTimeout(function() {

            // set up SVG for D3
            var width = d3.select(ele[0])[0][0].offsetWidth - margin,
            height = 700/*scope.data.length * (barHeight + barPadding)*/,
            colors = d3.scale.category20();

            // required if we ever want to linearly scale down the size of the content
            var xScale = d3.scale.linear()
            .domain([-width / 2, width / 2])
            .range([0, width]);
            var yScale = d3.scale.linear()
            .domain([-height / 2, height / 2])
            .range([height, 0]);

            // set the height and width of the container
            svg.attr('width', width)
            .attr('height', height);

            // create our zoomable group
            var zoom = d3.behavior.zoom()
            .x(xScale)
            .y(yScale)
            .center([width / 2, height / 2])
            .scaleExtent([0.5, 5])
            .on("zoom", zoomed);
            var fake = d3.behavior.zoom();
            var fakeDrag = d3.behavior.drag();

            // and append the events to this zoomable group
            var vis = svg
            .append('svg:g')
            // .call(zoom)
            .on("dblclick.zoom", null)
            .append('svg:g')
            .on("mousedown", mousedown)
            .on("mouseup", mouseup)

            // the rectangle is useful for having a background thats white
            vis.append('svg:rect')
            .attr('width', 5 * width)
            .attr('height', 5 * height)
            .attr('fill', 'white')
            .attr('stroke', 'lightgray')
            .attr("x", function(d) {return -((5*width)/2);})
            .attr("y", function(d) {return -((5*height)/2);});


            // defs.append('svg:marker')
            // .attr('id', 'end-arrow')
            // .attr('viewBox', '0 -5 10 10')
            // .attr('refX', 6)
            // .attr('markerWidth', 3)
            // .attr('markerHeight', 3)
            // .attr('orient', 'auto')
            // .append('svg:path')
            // .attr('d', 'M0,-5L10,0L0,5')
            // .attr('fill', '#000')
            // defs.append('svg:marker')
            // .attr('id', 'start-arrow')
            // .attr('viewBox', '0 -5 10 10')
            // .attr('refX', 4)
            // .attr('markerWidth', 3)
            // .attr('markerHeight', 3)
            // .attr('orient', 'auto')
            // .append('svg:path')
            // .attr('d', 'M10,-5L0,0L10,5')
            // .attr('fill', '#000');
            


            // set up initial nodes and links
            //  - nodes are known by 'id', not by index in array.
            //  - reflexive edges are indicated on the node (as a bold black circle).
            //  - links are always source < target; edge directions are set by 'left' and 'right'.
            var nodes = scope.nodes,
            lastNodeId = scope.nodes[scope.nodes.length -1].id,
            links = scope.links;

            var text = vis.append("svg:g");
            _.each(links, function(link, i){              
              text.append("svg:text")
              .attr("x", 20)
              .attr("dy", 13)
              .append("textPath")
              .attr('startOffset', '25%')
              .style("text-anchor","middle")
              .attr("font-family","FontAwesome")
              .attr("font-weight", "bold")
              .attr("xlink:href", "#path_" + i)
              .text(link.label + ' \uf178');
            })
            // init D3 force layout
            var force = d3.layout.force()
            .nodes(nodes)
            .links(links)
            .size([width, height])
            .linkDistance(225)
            .charge(-3000)
            .on('tick', tick)

            // define arrow markers for graph links

            // handles to link and node element groups
            var path = vis.append('svg:g').selectAll('path'),
            circle = vis.append('svg:g').selectAll('g');

            // mouse event vars
            var selected_node = null,
            selected_link = null,
            mousedown_link = null,
            mousedown_node = null,
            mouseup_node = null;

            function resetMouseVars() {
              mousedown_node = null;
              mouseup_node = null;
              mousedown_link = null;
            }

            // update force layout (called automatically each iteration)
            function tick() {
              // draw directed edges with proper padding from node centers
              path.attr('d', function(d) {
                var deltaX = d.target.x - d.source.x;
                var deltaY = d.target.y - d.source.y;
                var dist = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                var normX = deltaX / dist;
                var normY = deltaY / dist;
                normX = isNaN(normX)? 0: normX;
                normY = isNaN(normY)? 0: normY;
                var sourcePadding = d.left ? 12 : 12;
                //var sourcePadding = d.left ? 17 : 12,
                var targetPadding = d.right ? 12 : 12;
                //var targetPadding = d.right ? 17 : 12,
                var sourceX = d.source.x + (sourcePadding * normX);
                var sourceY = d.source.y + (sourcePadding * normY);
                var targetX = d.target.x - (targetPadding * normX);
                var targetY = d.target.y - (targetPadding * normY);
                var newD = '';

                // use this line instead of the return to make the paths arc
                // newD = linkArc(sourceX, sourceY, targetX, targetY);
                newD = 'M' + sourceX + ',' + sourceY + 'L' + targetX + ',' + targetY;
                return newD;
              });

              circle.attr('transform', function(d) { //
                return 'translate(' + d.x + ',' + d.y + ')';
              });

            }

            // update graph (called when needed)
            function restart() {
              // path (link) group
              // path = path.data(links, function(d) { return d.id; });
              path = path.data(links);

              // update existing links

              path.classed('selected', function(d) { return d === selected_link; })
              .style('marker-start', function(d) { return d.left ? 'url(#start-arrow)' : ''; })
              .style('marker-end', function(d) { return d.right ? 'url(#end-arrow)' : ''; });


              // add new links
              path.enter().append('svg:path')
              .attr('class', 'link')
              .attr("id", function (d,i) { return "path_" + i; })
              .classed('selected', function(d) { return d === selected_link; })
              .style('marker-start', function(d) { return d.left ? 'url(#start-arrow)' : ''; })
              .style('marker-end', function(d) { return d.right ? 'url(#end-arrow)' : ''; })
              .on('mousedown', function(d) {
                if(d3.event.ctrlKey || checkTrue(scope.readonly)) return;
                // select link
                mousedown_link = d;
                if(mousedown_link === selected_link) selected_link = null;
                else selected_link = mousedown_link;
                selected_node = null;
                restart();
              })

              path.exit().remove();
              // not workin in IE? We get ghosting of the paths...


              // circle (node) group
              // NB: the function arg is crucial here! nodes are known by id, not by index!
              circle = circle.data(nodes, function(d) { return d.id; });

              // update existing nodes (reflexive & selected visual states)
              circle.selectAll('circle')
              .style('fill', function(d) { return (d === selected_node) ? d3.rgb(colors(d.id)).brighter().toString() : colors(d.id); })
              .classed('reflexive', function(d) { return d.reflexive; });

              // add new nodes
              var g = circle.enter().append('svg:g');

              g.append('svg:circle')
              .attr('class', 'node')
              .attr('r', 12)
              .style('fill', function(d) { return (d === selected_node) ? d3.rgb(colors(d.id)).brighter().toString() : colors(d.id); })
              .style('stroke', function(d) { return d3.rgb(colors(d.id)).darker().toString(); })
              .style('opacity', '.3')
              .classed('reflexive', function(d) { return d.reflexive; })
              .on('mouseover', function(d) {
                if(!mousedown_node || d === mousedown_node) return;
                // enlarge target node
                d3.select(this).attr('transform', 'scale(1.1)');
              })
              .on('mouseout', function(d) {
                if(!mousedown_node || d === mousedown_node) return;
                // unenlarge target node
                d3.select(this).attr('transform', '');
              })
              .on('mousedown', function(d) {
                // select node
                mousedown_node = d;
                if(mousedown_node === selected_node) selected_node = null;
                else selected_node = mousedown_node;
                selected_link = null;

                vis.call(fake);
                circle.call(force.drag);
                restart();
              })
              .on('mouseup', function(d) { //
                if(!mousedown_node) return;
                // check for drag-to-self
                mouseup_node = d;
                if(mouseup_node === mousedown_node) { resetMouseVars(); return; }

                // unenlarge target node
                // d3.select(this).attr('transform', '');

                // add link to graph (update if exists)
                // NB: links are strictly source < target; arrows separately specified by booleans
                var source, target, direction;
                if(mousedown_node.id < mouseup_node.id) {
                  source = mousedown_node;
                  target = mouseup_node;
                  direction = 'right';
                } else {
                  source = mouseup_node;
                  target = mousedown_node;
                  direction = 'left';
                }

                var link;
                link = links.filter(function(l) {
                  return (l.source === source && l.target === target);
                })[0];

                if(link) {
                  link[direction] = true;
                } else {
                  link = {source: source, target: target, left: false, right: false};
                  link[direction] = true;
                  links.push(link);
                }

                // select new link
                selected_link = link;
                selected_node = null;

                vis.call(zoom);
                circle.call(fakeDrag);
                restart();
              })
              .on('dblclick', function(d){ //
                scope.onClick({
                  'location': {
                    path: '/single',
                    search: {'id': d.id}
                  }
                })
              })

              // show node IDs
              g.append('svg:text')
              .attr('x', 0)
              .attr('y', 5)
              .attr('class', 'id')
              .text(function(d) { return d.label; });

              // remove old nodes
              circle.exit().remove();


              // set the graph in motion
              force.start();
              if(!scope.$$phase) {
                scope.$apply();
              }
            }

            function mousedown() {
              if (!mousedown_node && !mousedown_link) {
                vis.call(zoom);
                circle.call(fakeDrag);
                return;
              } 
            }

            function mouseup() {
              // because :active only works in WebKit?
              vis.classed('active', false);

              // clear mouse event vars
              resetMouseVars();
            }

            function spliceLinksForNode(node) {
              var toSplice = links.filter(function(l) {
                return (l.source === node || l.target === node);
              });
              toSplice.map(function(l) {
                links.splice(links.indexOf(l), 1);
              });
            }

            function keydown() {
            }

            function keyup() {
            }
            // rescale g
            function zoomed() {
              // console.log('d3.translate', d3.event.translate);
              vis.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
            }

            function dragstarted(d) {
              d3.event.sourceEvent.stopPropagation();
              d3.select(this).classed("dragging", true);
            }

            function dragged(d) {
              d3.select(this).attr("cx", d.x = d3.event.x).attr("cy", d.y = d3.event.y);
            }

            function dragended(d) {
              d3.select(this).classed("dragging", false);
            }
            function linkArc(sourceX, sourceY, targetX, targetY) {
              var dx = targetX - sourceX,
              dy = targetY - sourceY,
              dr = Math.sqrt(dx * dx + dy * dy);
              return "M" + sourceX + "," + sourceY + "A" + dr + "," + dr + " 0 0,1 " + targetX + "," + targetY;
            }


            // app starts here
            d3.select(window)
            .on('keydown', keydown)
            .on('keyup', keyup);
            restart();
            resetMouseVars();
          }, 200);
        }; // end render
      });
    } // end link
  }
}])
.directive('d3DirectedGraph2', ['$window', '$timeout', 'd3Service', '$location', function($window, $timeout, d3Service, $location) {
  return {
    restrict: 'EA',
    scope: {
      owner: '=',
      data: '=',
      label: '@',
      readonly: '@',
      onClick: '&'
    },
    link: function(scope, ele, attrs) {
      // console.log('scope', scope);
      console.log('scope', angular.copy(scope.data));
      
      var checkTrue = function(value){
        if (value === 'true' || value === true || value === 1 || value === '1')
          return true;
        return false;
      }
      scope.nodes = [];
      scope.links = [];
      scope.owner = scope.owner || {};
      scope.owner.name = scope.owner.name || '';
      scope.owner.componentId = scope.owner.componentId || 0;

      (function(data){
        var tNodes = [];
        var tLinks = [];
        _.each(data, function(item){
          var source = _.find(tNodes, {'id': item.ownerComponentId});
          var target = _.find(tNodes, {'id': item.targetComponentId});
          if (!source) {
            source = {
              'id': item.ownerComponentId,
              'reflexive': false,
              'label': item.ownerComponentName
            }
            tNodes.push(source);
          }
          if (!target){
            target = {
              'id': item.targetComponentId,
              'reflexive': false,
              'label': item.targetComponentName
            }
            tNodes.push(target); 
          }
          if (source && target){

            tLinks.push({
              'source': source,
              'target': target,
              'left': false,
              'right': true,
              'label': item.relationshipTypeDescription,
              'type': item.relationshipType,
              'relId': item.relationshipId
            })
          }
        })
        // console.log('scope', scope);
        scope.nodes = tNodes; //
        scope.links = tLinks;
        scope.nodes = scope.nodes ||  [
        {id: 0, reflexive: false, label: 'this is cool'},
        {id: 1, reflexive: true , label: 'this is cool2'},
        {id: 2, reflexive: false, label: 'this is cool3'}
        ],
        scope.links = scope.links || [
        {source: scope.nodes[0], target: scope.nodes[1], left: false, right: true, label: 'relates to' },
        {source: scope.nodes[1], target: scope.nodes[2], left: false, right: true, label: 'depends on' },
        {source: scope.nodes[2], target: scope.nodes[1], left: false, right: true, label: 'Integrates with' }
        ];
      })(scope.data)
      // scope.nodes = [
      // {id: 0, reflexive: false, label: 'this is cool'},
      // {id: 1, reflexive: true , label: 'this is cool2'},
      // {id: 2, reflexive: false, label: 'this is cool3'}
      // ],
      // scope.links = [
      // {source: scope.nodes[0], target: scope.nodes[1], left: false, right: true, label: 'relates to' },
      // {source: scope.nodes[1], target: scope.nodes[2], left: false, right: true, label: 'depends on' },
      // {source: scope.nodes[2], target: scope.nodes[1], left: false, right: true, label: 'Integrates with' }
      // ];

      d3Service.d3().then(function(d3) {
        var renderTimeout;
        var margin = parseInt(attrs.margin) || 20,
        ballRadius = parseInt(attrs.ballRadius) || 5;

        var svg = d3.select(ele[0])
        .append('svg:svg')
        .style('width', '100%');
        console.log('svg', svg);
        
        svg[0][0].setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/svg");
        svg[0][0].setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xlink", "http://www.w3.org/1999/xlink");

        $window.onresize = function() {
          scope.$apply();
        };

        scope.$watch(function() {
          return angular.element($window)[0].innerWidth;
        }, function() {
          scope.render(scope.data);
        });

        scope.$watch('data', function(newData) {
          scope.render(newData);
        }, true);

        scope.render = function(data) {
          scope.absUrl = $location.absUrl();
          svg.selectAll('*').remove();
          if (!data) return;
          if (renderTimeout) clearTimeout(renderTimeout);

          renderTimeout = setTimeout(function() {

            // set up SVG for D3
            var width = d3.select(ele[0])[0][0].offsetWidth - margin,
            height = 700/*scope.data.length * (barHeight + barPadding)*/,
            colors = d3.scale.category20();

            // required if we ever want to linearly scale down the size of the content
            var xScale = d3.scale.linear()
            .domain([-width / 2, width / 2])
            .range([0, width]);
            var yScale = d3.scale.linear()
            .domain([-height / 2, height / 2])
            .range([height, 0]);

            // set the height and width of the container
            svg.attr('width', width)
            .attr('height', height);

            var nodes = scope.nodes,
            lastNodeId = scope.nodes[scope.nodes.length -1].id,
            links = scope.links;

            // Compute the distinct nodes from the links.
            links.forEach(function(link) {
              var foundSource = _.find(nodes, link.source);
              var foundTarget = _.find(nodes, link.target);
              link.source = foundSource || 
              (nodes[link.source] = {name: link.source});
              link.target = foundTarget || 
              (nodes[link.target] = {name: link.target});
            });
            console.log('links', links);
            console.log('nodes', nodes);



            var width = 960,
            height = 500;

            var force = d3.layout.force()
            .nodes(d3.values(nodes))
            .links(links)
            .size([width, height])
            .linkDistance(300)
            .charge(-300)
            .on("tick", tick)
            .start();

            // build the arrow.
            svg.append("svg:defs").selectAll("marker")
            .data(["end"])      // Different link/path types can be defined here
            .enter().append("svg:marker")    // This section adds in the arrows
            .attr("id", String)
            .attr("viewBox", "0 -5 10 10")
            .attr("refX", 20)
            .attr("refY", -1.5)
            .attr("markerWidth", 6)
            .attr("markerHeight", 6)
            .attr("orient", "auto")
            .append("svg:path")
            .attr("d", "M0,-5L10,0L0,5");

            var text = svg.append("svg:g");
            _.each(links, function(link, i){              
              text.append("svg:text")
              .attr("x", 20)
              .attr("dy", -13)
              .append("textPath")
              .attr('startOffset', '25%')
              .style("text-anchor","middle")
              .attr("font-family","FontAwesome")
              .attr("font-weight", "bold")
              .attr("xlink:href", "#path_" + i)
              .text(link.label + ' \uf178');
            })

            // add the links and the arrows
            var path = svg.append("svg:g").selectAll("path")
            .data(force.links())
            .enter().append("svg:path")
            //    .attr("class", function(d) { return "link " + d.type; })
            .attr("class", "link")
            .attr("id", function (d,i) { return "path_" + i; })
            .attr("marker-end", "url(" + scope.absUrl + "#end)");

            // define the nodes
            var node = svg.selectAll(".node")
            .data(force.nodes())
            .enter().append("g")
            .attr("class", "node")
            .call(force.drag);

            // add the nodes
            node.append("circle")
            .attr("r", 10);

            // add the text 
            node.append("text")
            .attr("x", 12)
            .attr("dy", ".35em")
            .text(function(d) { return d.label; });

            // add the curvy lines
            function tick() {
              path.attr("d", function(d) {
                var dx = d.target.x - d.source.x,
                dy = d.target.y - d.source.y,
                dr = Math.sqrt(dx * dx + dy * dy);
                return "M" + 
                d.source.x + "," + 
                d.source.y + "A" + 
                dr + "," + dr + " 0 0,1 " + 
                d.target.x + "," + 
                d.target.y;
              });

              node
              .attr("transform", function(d) { 
                return "translate(" + d.x + "," + d.y + ")"; });
            }
          }, 200);
        }//
      });
    } // end link
  }
}])
.directive('d3DirectedGraph', ['$window', '$timeout', 'd3Service', '$location', function($window, $timeout, d3Service, $location) {
  return {
    restrict: 'EA',
    scope: {
      owner: '=',
      data: '=',
      label: '@',
      readonly: '@',
      onClick: '&'
    },
    link: function(scope, ele, attrs) {
      // console.log('scope', scope);
      console.log('scope', angular.copy(scope.data));
      
      var checkTrue = function(value){
        if (value === 'true' || value === true || value === 1 || value === '1')
          return true;
        return false;
      }
      scope.nodes = [];
      scope.links = [];
      scope.owner = scope.owner || {};
      scope.owner.name = scope.owner.name || '';
      scope.owner.componentId = scope.owner.componentId || 0;

      (function(data){
        var tNodes = [];
        var tLinks = [];
        _.each(data, function(item){
          var source = _.find(tNodes, {'id': item.ownerComponentId});
          var target = _.find(tNodes, {'id': item.targetComponentId});
          if (!source) {
            source = {
              'id': item.ownerComponentId,
              'reflexive': false,
              'label': item.ownerComponentName
            }
            tNodes.push(source);
          }
          if (!target){
            target = {
              'id': item.targetComponentId,
              'reflexive': false,
              'label': item.targetComponentName
            }
            tNodes.push(target); 
          }
          if (source && target){

            tLinks.push({
              'source': source,
              'target': target,
              'left': false,
              'right': true,
              'label': item.relationshipTypeDescription,
              'type': item.relationshipType,
              'relId': item.relationshipId
            })
          }
        })
        // console.log('scope', scope);
        scope.nodes = tNodes; //
        scope.links = tLinks;
        scope.nodes = scope.nodes ||  [
        {id: 0, reflexive: false, label: 'this is cool'},
        {id: 1, reflexive: true , label: 'this is cool2'},
        {id: 2, reflexive: false, label: 'this is cool3'}
        ],
        scope.links = scope.links || [
        {source: scope.nodes[0], target: scope.nodes[1], left: false, right: true, label: 'relates to' },
        {source: scope.nodes[1], target: scope.nodes[2], left: false, right: true, label: 'depends on' },
        {source: scope.nodes[2], target: scope.nodes[1], left: false, right: true, label: 'Integrates with' }
        ];
      })(scope.data)
      // scope.nodes = [
      // {id: 0, reflexive: false, label: 'this is cool'},
      // {id: 1, reflexive: true , label: 'this is cool2'},
      // {id: 2, reflexive: false, label: 'this is cool3'}
      // ],
      // scope.links = [
      // {source: scope.nodes[0], target: scope.nodes[1], left: false, right: true, label: 'relates to' },
      // {source: scope.nodes[1], target: scope.nodes[2], left: false, right: true, label: 'depends on' },
      // {source: scope.nodes[2], target: scope.nodes[1], left: false, right: true, label: 'Integrates with' }
      // ];

      d3Service.d3().then(function(d3) {
        var renderTimeout;
        var margin = parseInt(attrs.margin) || 20,
        ballRadius = parseInt(attrs.ballRadius) || 5;

        var svg = d3.select(ele[0])
        .append('svg:svg')
        .style('width', '100%');
        console.log('svg', svg);
        
        svg[0][0].setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/svg");
        svg[0][0].setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xlink", "http://www.w3.org/1999/xlink");

        $window.onresize = function() {
          scope.$apply();
        };

        scope.$watch(function() {
          return angular.element($window)[0].innerWidth;
        }, function() {
          scope.render(scope.data);
        });

        scope.$watch('data', function(newData) {
          scope.render(newData);
        }, true);

        scope.render = function(data) {
          scope.absUrl = $location.absUrl();
          svg.selectAll('*').remove();
          if (!data) return;
          if (renderTimeout) clearTimeout(renderTimeout);

          renderTimeout = setTimeout(function() {

            // set up SVG for D3
            var width = d3.select(ele[0])[0][0].offsetWidth - margin,
            height = 700/*scope.data.length * (barHeight + barPadding)*/,
            colors = d3.scale.category20();

            // required if we ever want to linearly scale down the size of the content
            var xScale = d3.scale.linear()
            .domain([-width / 2, width / 2])
            .range([0, width]);
            var yScale = d3.scale.linear()
            .domain([-height / 2, height / 2])
            .range([height, 0]);

            // set the height and width of the container
            svg.attr('width', width)
            .attr('height', height);

            var nodes = scope.nodes,
            lastNodeId = scope.nodes[scope.nodes.length -1].id,
            links = scope.links;

            // Compute the distinct nodes from the links.
            links.forEach(function(link) {
              var foundSource = _.find(nodes, link.source);
              var foundTarget = _.find(nodes, link.target);
              link.source = foundSource || 
              (nodes[link.source] = {label: link.source});
              link.target = foundTarget || 
              (nodes[link.target] = {label: link.target});
            });
            console.log('links', links);
            console.log('nodes', nodes);

            //Set up tooltip
            var tip = d3.tip()
            .attr('class', 'd3-tip')
            .html(function (d) {
              return  d.label + "";
            })
            // .direction(function (d) {
            //   var dy = d.target.y - d.source.y;
            //   var dx = d.target.x - d.source.x;
            //   var theta = Math.atan2(dy, dx); // range (-PI, PI]
            //   //if (theta < 0) theta = 360 + theta; // range [0, 360)
            //   theta *= 180 / Math.PI; // rads to degs, range (-180, 180]
            //   while(theta < 0) {
            //     theta += 360;
            //   }
            //   while (theta > 360){
            //     theta -= 360;
            //   }
            //   console.log('theta', theta);//
            //   if (theta < 29 || theta >= 343){
            //     return 'ne'
            //   } else if (theta < 343 && theta >= 297) {
            //     return 'n'
            //   } else if (theta < 297 && theta >= 253) {
            //     return 'nw'
            //   } else if (theta < 253 && theta >= 208) {
            //     return 'w'
            //   } else if (theta < 208 && theta >= 163) {
            //     return 'sw'
            //   } else if (theta < 163 && theta >= 118) {
            //     return 's'
            //   } else if (theta < 118 && theta >= 73) {
            //     return 'se'
            //   } else if (theta < 73 && theta >= 29) {
            //     return 'e'
            //   }
            // })
            svg.call(tip);

            //Set up the colour scale
            var color = d3.scale.category20();

            //Set up the force layout
            var force = d3.layout.force()
            .charge(-3000)
            .linkDistance(225)
            .size([width, height]);

            //Creates the graph data structure out of the json data
            force.nodes(nodes)
            .links(links)
            .start();

            //Create all the line svgs but without locations yet
            var link = svg.selectAll(".link")
            .data(links)
            .enter().append("svg:path")
            .attr("class", "link")
            .attr("id", function (d,i) { return "path_" + i; })
            .style("marker-end",  "url(" + scope.absUrl + "#suit)") //Added 
            .on('mouseover', tip.show) //Added
            .on('mouseout', tip.hide); //Added 
            ;

            //Do the same with the circles for the nodes - no 
            var node = svg.selectAll(".node")
            .data(force.nodes())
            .enter().append("g")
            .attr("class", "node")
            .call(force.drag);

            // add the nodes
            node.append("svg:circle")
            .attr("r", 10)
            .style('fill', function(d) { return color(d.id); })
            .style('stroke', function(d) { return d3.rgb(color(d.id)).darker().toString(); })

            // add the text 
            node.append("text")
            .attr("x", 12)
            .attr("dy", ".35em")
            .text(function(d) { return d.label; });

            //Now we are giving the SVGs co-ordinates - the force layout is generating the co-ordinates which this code is using to update the attributes of the SVG elements
            force.on("tick", function () {

              link.attr("d", function(d) {
                var dx = d.target.x - d.source.x,
                dy = d.target.y - d.source.y,
                dr = Math.sqrt(dx * dx + dy * dy);
                return "M" + 
                d.source.x + "," + 
                d.source.y + "A" + 
                dr + "," + dr + " 0 0,1 " + 
                d.target.x + "," + 
                d.target.y;
              });

              node.attr("transform", function(d) { 
                return "translate(" + d.x + "," + d.y + ")"; });
            }); //

            svg.append("defs").selectAll("marker")
            .data(["suit", "licensing", "resolved"])
            .enter().append("marker")
            .attr("id", function(d) { return d; })
            .attr("viewBox", "0 -5 10 10")
            .attr("refX", 17)
            .attr("refY", 0)
            .attr("markerWidth", 3)
            .attr("markerHeight", 3)
            .attr("orient", "auto")
            .append("path")
            .attr("d", "M0,-5L10,0L0,5 L10,0 L0, -5")
            .style("stroke", "#4679BD")
            .style("stroke-width", "2px")
            .style("opacity", "0.6");
          }, 200);
        }//
      });
    } // end link
  }
}])