/**
 * Created by besplin on 8/20/2015.
 * From:  http://angular-tips.com/blog/2014/03/introduction-to-unit-test-spies/
 */
'use strict';

// This is the one we don't care about
function RestService() {
}

// See http://www.w3schools.com/js/js_object_prototypes.asp
RestService.prototype.init = function () {
  // put init stuff here
};

RestService.prototype.getAll = function () {
  return[];  // Return elements
};

// This is our SUT (Subject under test)
function Post(rest) {
  this.rest = rest;
  rest.init();
}

Post.prototype.retrieve = function() {
  this.posts = this.rest.getAll();
};

Post.prototype.accept = function(item, callback) {
  this.rest.update(item);
  if(callback) {
    callback();
  }
};

/* We have here our SUT which is a Post constructor.  It uses a RestService to fetch its stuff.
    Our Post will delegate all the Rest work to the RestService which will be initialized when we create a new Post object.
    Let’s start testing it step by step:
 */

describe('Posts', function() {
  var rest, post;

  beforeEach(function() {
    rest = new RestService();
    post = new Post(rest);
  });
});

// Upon Post creation, we initialize the RestService. We want to test that, how can we do that?:
it('will initialize the rest service upon creation', function() {
  spyOn(rest, 'init');

  post = new Post(rest);
  expect(rest.init).toHaveBeenCalled();
});

// Something important here is that the when you spy a function, the real function is never called.
// So here rest.init doesn’t actually run.

it('will receive the list of posts from the rest service', function() {
  var posts = [
    {
      title: 'Foo',
      body: 'Foo post'
    },
    {
      title: 'Bar',
      body: 'Bar post'
    }
  ];

  spyOn(rest, 'getAll').and.returnValue(posts);
  post.retrieve();
  expect(rest.getAll).toHaveBeenCalled();
  expect(post.posts).toBe(posts);
});
