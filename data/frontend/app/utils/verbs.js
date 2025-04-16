const verbs = {
  get: async function (serviceUri, endpoint, config = {}) {
    const res = await fetch(serviceUri + endpoint, {
      method: "GET",
      ...config,
    });
    return res.json();
  },
  post: async function (serviceUri, endpoint, body, config = {}) {
    const res = await fetch(serviceUri + endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        ...(config.headers || {}),
      },
      body: JSON.stringify(body),
      ...config,
    });
    return res.json();
  },
  put: async function (serviceUri, endpoint, body, config = {}) {
    const res = await fetch(serviceUri + endpoint, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        ...(config.headers || {}),
      },
      body: JSON.stringify(body),
      ...config,
    });
    return res.json();
  },
  delete: async function (serviceUri, endpoint, config = {}) {
    const res = await fetch(serviceUri + endpoint, {
      method: "DELETE",
      ...config,
    });
    return res.json();
  },
};

export default verbs;
