/**
 * This method transforms the given number of bytes into a string representation of the number using SI prefixes
 * @param {Number} numBytes The size in bytes to translate
 * @returns {String} A string representing a human readable, truncated form of the input number of bytes
 * Source: https://stackoverflow.com/a/20732091/8223925
 * Examples:
 *   humanReadableBytes(0) // "0 B"
 *   humanReadableBytes(1000) // "1000 B"
 *   humanReadableBytes(1026) // "1 kB"
 *   humanReadableBytes(10260000) // "9.78 MB"
 */
export function humanReadableBytes(numBytes) {
  const i = numBytes === 0 ? 0 : Math.floor(Math.log(numBytes) / Math.log(1024))
  return Number((numBytes / Math.pow(1024, i)).toFixed(2)) + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i]
}

export const MiBtoBytes = 1048576

/**
 * Check Mime type by sniffing the first few bytes of the file. Derived from https://gist.github.com/topalex/ad13f76150e0b36de3c4a3d5ba8dc63a
 * @param {File} file Input file to check.
 * @param {Function} callback Function to call with the derived type. This function should have the signature `callback(type:string)`.
 */
export function getFileTypeFromSignature(file, callback) {
  let blob = file.slice(0, 4)
  let fileReader = new FileReader()
  fileReader.onloadend = function(e) {
    let bytes = new Uint8Array(e.target.result)
    let header = ''
    for (let i = 0; i < bytes.length; i++) {
      header += bytes[i].toString(16)
    }

    // Check the file signature against known types. Source: https://en.wikipedia.org/wiki/List_of_file_signatures
    // Note: Wikipedia displays hex values in two byte chunks which don't always get turned into strings like you may expect
    // for example, hex 03 becomes 3 in the header but hex 50 remains 50 in the header.
    let type = 'unknown'
    switch (header) {
      case '89504e47':
        type = 'image/png'
        break
      case '47494638':
        type = 'image/gif'
        break
      case 'ffd8ffe0':
      case 'ffd8ffe1':
      case 'ffd8ffe2':
        type = 'image/jpeg'
        break
      case '25504446':
        type = 'application/pdf'
        break
      case '504b34':
      case '504b56':
      case '504b78':
        type = 'application/zip'
        break
    }

    callback(type)
  }
  fileReader.readAsArrayBuffer(blob)
}

export default { humanReadableBytes, MiBtoBytes, getFileTypeFromSignature }
