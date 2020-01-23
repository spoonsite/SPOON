module.exports = {
  root: true,
  env: {
    node: true
  },
<<<<<<< HEAD
  extends: ['plugin:vue/essential', '@vue/standard'],
=======
  extends: [
    'plugin:vue/essential',
    '@vue/standard'
  ],
>>>>>>> origin/develop
  rules: {
    'space-before-function-paren': ['error', 'never'],
    'no-console': process.env.NODE_ENV === 'production' ? ['error', { allow: ['error'] }] : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off'
  },
  parserOptions: {
    parser: 'babel-eslint'
  },
  overrides: [
    {
      files: ['**/__tests__/*.{j,t}s?(x)'],
      env: {
        mocha: true
      }
    }
  ]
}
