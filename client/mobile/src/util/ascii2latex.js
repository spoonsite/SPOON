import katex from 'katex';
import ascii2latex from 'asciimath-to-latex';

/**
 * Render asciiMath to Katex output
 *
 * @param {String} string - asciimath input
 * @param {Object} katexOpts - Katex Options see https://katex.org/docs/options.html
 */
function renderAsciiMath(string, katexOpts) {
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
function asciiToKatex(str, block) {
  if (str !== undefined) {

    // if block == true it will return a block element, else it will return an inline element
    // regex for placing quotes in str before parsing to katex to ensure sub-units are grouped right 
    // ex: kg/m -> "kg"/"m"
    const regex = new RegExp("([a-zA-Z$]{1,})+", "gu");
    str = str.replace(regex, '"$&"');

    // This is for a specific JScience case where ^1/2 is resolved as 1:2, 
    // this is not a recognized ascii math method so it is changed 
    // back to 1/2 so katex can understand it.
    const JScienceRegex = new RegExp("((\\d+):(\\d+))+", "g");
    str = str.replace(JScienceRegex, '($2/$3)');

    // Katex does not allow $ when converting so this is the way 
    // of fixing that edge case. (cases like this should not 
    // be in the database though)
    if (str == '"$"') {
      return ("$");
    }

    // method for converting ascii to katex
    try {
      var katex = renderAsciiMath(str, { displayMode: !!block, throwOnError: true });
      return katex;
    } catch (err) {
      return err;
    }

  } else {
    return "";
  }
}

export default asciiToKatex;
