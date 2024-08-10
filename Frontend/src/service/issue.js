import {apiURL, post} from './util'

export async function issueService(newitem) {
  let res = await post(`${apiURL}/service/issue`, newitem)
  return res;
}

export async function issueTask(newitem) {
  let res = await post(`${apiURL}/task/issue`, newitem)
  return res;
}