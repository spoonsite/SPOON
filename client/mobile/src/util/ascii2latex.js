import katex from 'katex';
import ascii2latex from 'asciimath-to-latex';

/**
 * Render asciiMath to Katex output
 *
 * @param {String} string - asciimath input
 * @param {Object} katexOpts - Katex Options see https://katex.org/docs/options.html
 */
function renderAsciiMath (string, katexOpts) {
  // regex stuff here
  var latex = ascii2latex(string);
  return katex.renderToString(latex, katexOpts);
}

/**
 * process the asciimath input to rendered latex
 * regex stuff taken from server\openstorefront\openstorefront-web\src\main\webapp\scripts\util\coreUtil.js
 * asciiToKatex
 *
 * @param {String} str input asciimath string to pass to katex
 * @param {Boolean} block configure katex block element
 * @returns render katex html
 */
function asciiToKatex (str, block) {
  // if block == true it will return a block element, else it will return an inline element
  // regex for placing parentheses in str before parsing to katex to ensure sub-units are grouped right
  // ex: kg/m -> (kg)/m

  const regex = /(\w{1,})+/gu;

  // regex for JScience edge case
  const JScienceRegex = /((\d+):(\d+))+/g;

  var regexArray = [];
  var JScienceRegexArray = [];

  if (str !== undefined) {
    // This is for a specific JScience case where ^1/2 is resolved as 1:2,
    // this is not a recognized ascii math method so it is changed
    // back to 1/2 so katex can understand it.
    var m;
    while ((m = JScienceRegex.exec(str)) !== null) {
      if (m.index === JScienceRegex.lastIndex) {
        JScienceRegex.lastIndex++;
      }
      // replacement with parenthesized versions of the sub-units
      // with check to see if the sub-unit is the whole unit
      // ex: kg
      JScienceRegexArray.push(m[0]);
    }
    // iterate over found matches and place quotes around
    // the matches in the current string
    JScienceRegexArray.forEach(function (match) {
      let newSubStr = '(' + match.replace(':', '/') + ')';
      str = str.replace(match, newSubStr);
    });

    // this is the regex for any text in the units
    while ((m = regex.exec(str)) !== null) {
      if (m.index === regex.lastIndex) {
        regex.lastIndex++;
      }

      // replacement with parenthesized versions of the sub-units
      // with check to see if the sub-unit is the whole unit
      // ex: kg
      regexArray.push(m[0]);
    }

    // iterate over found matches and place parenthesis around
    // the matches in the current string
    regexArray.forEach(function (match) {
      let parenMatch = '"' + match + '"';
      str = str.replace(match, parenMatch);
    });

    // Katex does not allow $ when converting so this is the way
    // of fixing that edge case. (cases like this should not
    // be in the database though)
    str = '"' + str.trim() + '"';
    if (str === '"$"' || str === '$') {
      return ('$');
    }
    str = str.slice(1, -1);

    // method for converting ascii to katex
    var katex = renderAsciiMath(str, { displayMode: block });
    return katex;
  } else {
    return '';
  }
}

export default asciiToKatex;
