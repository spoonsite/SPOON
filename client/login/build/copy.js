const fs = require('fs-extra');

const from = './dist';
const to = '../../server/openstorefront/openstorefront-web/src/main/webapp/login';

// Async with promises:
fs.copy(from, to)
  .then(() => console.log(`successfully copied ${from} to ${to}`))
  .catch(err => console.error(err));
