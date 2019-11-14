/**
 * Change number string in scientific notation (e.g. "12e-4") to decimal notation ("0.0012")
 * https://gist.github.com/jiggzson/b5f489af9ad931e3d186
 * @param {String} number
 * @returns {String} Number in decimal notation.
 */
export function scientificToDecimal (number) {
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
/**
 * This method will check if the inputNumber is actually a number
 * and if it is, it will reduce it's length in a meaningful way.
 * ParseFloat info found at https://stackoverflow.com/questions/3612744/remove-insignificant-trailing-zeros-from-a-number
 * @param {String} inputNumber
 * @returns {String} The modified string if able to modify, otherwise return original inputNumber
 */
export function crushNumericString (inputNumber) {
  // If inputNumber is not a number return.
  if (isNaN(inputNumber)) {
    if (typeof inputNumber === 'string' || inputNumber instanceof String) {
      return inputNumber.slice(0, 40) + '...'
    }
    return inputNumber
  }
  // If it contains an E or e don't touch it and return.
  if (inputNumber.indexOf('E') !== -1) {
    inputNumber = scientificToDecimal(inputNumber)
  }
  if (inputNumber.indexOf('e') !== -1) {
    inputNumber = scientificToDecimal(inputNumber)
  }

  var magnitudeIsGreaterThanOne = false
  var numberLength = inputNumber.length

  // Is the absolute value of this number bigger than one?
  if (Math.abs(inputNumber) > 1) {
    magnitudeIsGreaterThanOne = true
  }

  // If it is take this route
  if (magnitudeIsGreaterThanOne) {
    if (inputNumber.indexOf('.') !== -1) {
      if ((numberLength - inputNumber.indexOf('.')) > 5) {
        return parseFloat(parseFloat(inputNumber.slice(0, inputNumber.indexOf('.') + 4)).toFixed(3))
      }
      return parseFloat(inputNumber)
    }
  }

  // Otherwise take this route
  if (!magnitudeIsGreaterThanOne) {
    // Find first non zero thing after the decimal and show 3 decimal places after it.
    var firstNonZeroIndex
    for (var i = 0; i < numberLength; i++) {
      if ((inputNumber[i] === '-') || (inputNumber[i] === '.') || (inputNumber[i] === '0')) {
        continue
      }
      firstNonZeroIndex = i
      break
    }
    if (numberLength - firstNonZeroIndex > 5) {
      return parseFloat(parseFloat(inputNumber.slice(0, firstNonZeroIndex + 4)).toFixed(firstNonZeroIndex + 1))
    }
    return parseFloat(inputNumber)
  }
  return parseFloat(inputNumber)
}

export default { crushNumericString, scientificToDecimal }
