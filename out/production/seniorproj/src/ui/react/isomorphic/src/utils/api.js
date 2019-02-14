/* These functions will be used for fetching and
   updating data through the server */

/* If we recieve a response, you can pass in a
   dispatch function and action so that we can
   dispatch an action which will update the store
   in the reducer */
module.exports = {
  get: (uri: string, dispatch = undefined, action = undefined) => {
    fetch(uri, {
      method: 'get',
    }).then(
      (response) => {
        if (response) {
          console.log('Recieved response');
          return response.json();
        } else {
          console.log('Error executing http get');
          return 0;
        }
      }
    )
      .then((data) => {
        console.log('data: ', data);
        if (dispatch !== undefined && action !== undefined) {
          console.log('Dispatching action!');
          dispatch(action);
        }
      })
      .catch((e) => {
        console.log(e);
      });
  },

  post: (uri: string, postBody: object, dispatch = undefined, action = undefined) => {
    fetch(uri, {
      method: 'post',
      body: JSON.stringify(postBody),
    }).then(
      (response) => {
        if (response) {
          console.log('Recieved response');
          return response.json();
        } else {
          console.log('Error executing http post');
          return 0;
        }
      }
    )
      .then((data) => {
        console.log('data: ', data);
        if (dispatch !== undefined && action !== undefined) {
          console.log('Dispatching action!');
          dispatch(action);
        }
      })
      .catch((e) => {
        console.log(e);
      });
  }
}
