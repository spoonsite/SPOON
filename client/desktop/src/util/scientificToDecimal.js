export default {
  /**
 * This method will change a string that is a number from scientific notation to decimal notation.
 * https://gist.github.com/jiggzson/b5f489af9ad931e3d186
 * @param {String} number
 * @returns {String} Number in decimal notation.
 */
  scientificToDecimal: function (number) {
    console.log('check123check123')
    let numberHasSign = number.startsWith('-') || number.startsWith('+')
    let sign = numberHasSign ? number[0] : ''
    number = numberHasSign ? number.replace(sign, '') : number

    // if the number is in scientific notation remove ii
    if (/\d+\.?\d*e[+-]*\d+/i.test(number)) {
      let zero = '0'
      let parts = String(number).toLowerCase().split('e') // split into coeff and exponent
      let e = parts.pop() // store the exponential part
      let l = Math.abs(e) // get the number of zeros
      let sign = e / l
      let coeffArray = parts[0].split('.')

      if (sign === -1) {
        coeffArray[0] = Math.abs(coeffArray[0])
        number = zero + '.' + new Array(l).join(zero) + coeffArray.join('')
      } else {
        let dec = coeffArray[1]
        if (dec) l = l - dec.length
        number = coeffArray.join('') + new Array(l + 1).join(zero)
      }
    }
    return sign + number
  }
}
