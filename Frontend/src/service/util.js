const credentials = 'include'

async function responseHandler(res) {
  if (res.status !== 200) throw res.status
  res = await res.json()
  if (!res.ok) throw res.message
  return res.data;
}

export async function get(url) {
  let headers = {'jwt': localStorage.getItem('jwt')}
  let res = await fetch(url, {method: 'GET', headers, credentials})
  return await responseHandler(res)
}

export async function post(url, data) {
  let headers = {'Content-Type': 'application/json', 'jwt': localStorage.getItem('jwt')}
  let res = await fetch(url, {method: 'POST', headers, body: JSON.stringify(data), credentials})
  return await responseHandler(res)
}

export async function put(url, data) {
  let headers = {'Content-Type': 'application/json', 'jwt': localStorage.getItem('jwt')}
  let res = await fetch(url, {method: 'PUT', headers, body: JSON.stringify(data), credentials})
  return await responseHandler(res)
}

export async function del(url, data) {
  let headers = {'Content-Type': 'application/json', 'jwt': localStorage.getItem('jwt')}
  let res = await fetch(url, {method: 'DELETE', headers, body: JSON.stringify(data), credentials})
  return await responseHandler(res)
}

export const apiURL = process.env.REACT_APP_API_URL