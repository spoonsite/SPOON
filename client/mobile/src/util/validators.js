export default {
  data: () => ({
    rules: {
      email: value => {
        // From https://stackoverflow.com/a/9204568 : /^[^\s@]+@[^\s@]+\.[^\s@]+$/ but this doesn't catch ".@..." or  "@\.\"
        // but does help the user catch common issues.
        // From http://emailregex.com/ : /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
        // but this doesn't match the valid address "user@[2001:DB8::1]" or catch '" "@example.org' as
        // invalid or support unicode in the domain name.
        // The intent here is to keep things VERY loose as DI2E and other groups
        // have had issues in the past and we need international support which adds whole other layers of complications
        const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return pattern.test(value) || 'Email address is not in a valid format';
      },
      required: value => {
        // Value cannot be empty or only whitespace
        return (!!value && /^\s+$/.test(value) === false) || 'This field is required';
      }
    }
  })
};
