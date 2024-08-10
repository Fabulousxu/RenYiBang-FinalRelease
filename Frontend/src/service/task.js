import {apiURL, get, put, del} from "./util";

export async function searchTask(keyword, pageSize, pageIndex, order, timeRange, priceRange) {
  let res = await get(`${apiURL}/task/search?keyword=${keyword}&pageSize=${pageSize}&pageIndex=${pageIndex}&order=${order}&timeBegin=${timeRange[0]}&timeEnd=${timeRange[1]}&priceLow=${priceRange[0]}&priceHigh=${priceRange[1]}`)
  for (let task of res.items) task.url = `/task/${task.taskId}`
  return res
}

export async function getTask(taskId) {
  return await get(`${apiURL}/task/${taskId}`)
}

export async function getTaskComment(taskId, pageSize, pageIndex, order) {
  let res = await get(`${apiURL}/task/${taskId}/comment?pageSize=${pageSize}&pageIndex=${pageIndex}&order=${order}`)
  for (let item of res.items) item.user = item.commenter
  return res
}

export async function getTaskMessage(taskId, pageSize, pageIndex, order) {
  let res = await get(`${apiURL}/task/${taskId}/message?pageSize=${pageSize}&pageIndex=${pageIndex}&order=${order}`)
  for (let item of res.items) item.user = item.messager
  return res
}

export async function collectTask(taskId) {
  return await put(`${apiURL}/task/${taskId}/collect`)
}

export async function uncollectTask(taskId) {
  return await del(`${apiURL}/task/${taskId}/uncollect`)
}

export async function likeComment(commentId) {
  return await put(`${apiURL}/task/comment/${commentId}/like`)
}

export async function unlikeComment(commentId) {
  return await del(`${apiURL}/task/comment/${commentId}/unlike`)
}

export async function likeMessage(messageId) {
  return await put(`${apiURL}/task/message/${messageId}/like`)
}

export async function unlikeMessage(messageId) {
  return await del(`${apiURL}/task/message/${messageId}/unlike`)
}

export async function putMessage(taskId, content) {
  return await put(`${apiURL}/task/${taskId}/message`, {content})
}

export async function putComment(taskId, content, rating) {
  return await put(`${apiURL}/task/${taskId}/comment`, {content, rating})

}

export async function accessTask(taskId) {
  let res = await put(`${apiURL}/task/${taskId}/access`)
  return res
}

export async function unaccessTask(taskId) {
  let res = await del(`${apiURL}/task/${taskId}/unaccess`)
  return res
}

// 发布者获取接取人列表
// /api/task/{taskId}/select/info
export async function getTaskSelectInfo(taskId, pageSize, pageIndex) {
  return await get(`${apiURL}/task/${taskId}/select/info?pageSize=${pageSize}&pageIndex=${pageIndex}`);
}

// 发布者取消已发布的任务
// /api/task/{taskId}/cancel
export async function cancelTask(taskId) {
  return await del(`${apiURL}/task/${taskId}/cancel`);
}

// 发布者选择接取人，创建订单
// /api/task/{taskId}/select/confirm
export async function confirmSelectTask(taskId, userList) {
  return await put(`${apiURL}/task/${taskId}/select/confirm`, {userList});
}

export async function refuseSelectTask(taskId, userList) {
  return await put(`${apiURL}/task/${taskId}/select/deny`, {userList});
}

export async function getTaskSuccessPeople(taskId, pageSize, pageIndex){
  return await get(`${apiURL}/task/${taskId}/select/success?pageSize=${pageSize}&pageIndex=${pageIndex}`);
}

export async function getTaskRefusePeople(taskId, pageSize, pageIndex){
  return await get(`${apiURL}/task/${taskId}/select/fail?pageSize=${pageSize}&pageIndex=${pageIndex}`);
}

//

export async function deleteComment(commentId) {
  return await del(`${apiURL}/task/comment/${commentId}`)
}

export async function deleteMessage(messageId) {
  return await del(`${apiURL}/task/message/${messageId}`)
}