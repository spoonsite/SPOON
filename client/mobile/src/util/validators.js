export default {
  data: () => ({
    emailRules: [
      value => !!value || 'This field is required',
      value => {
        // From https://stackoverflow.com/a/9204568
        return (
          /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value) ||
          'Email address not in valid format'
        );
      }
    ],
    inputRequired: [value => !!value || 'This field is required']
  })
};
