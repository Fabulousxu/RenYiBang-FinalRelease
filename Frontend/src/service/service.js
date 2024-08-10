import {apiURL, get, put, del} from "./util";

export async function searchService(keyword, pageSize, pageIndex, order, timeRange, priceRange) {
  let res = await get(`${apiURL}/service/search?keyword=${keyword}&pageSize=${pageSize}&pageIndex=${pageIndex}&order=${order}&timeBegin=${timeRange[0]}&timeEnd=${timeRange[1]}&priceLow=${priceRange[0]}&priceHigh=${priceRange[1]}`)
  for (let service of res.items) service.url = `/service/${service.serviceId}`
  return res
}

export async function getService(serviceId) {
  return await get(`${apiURL}/service/${serviceId}`)
}

export async function getServiceComment(serviceId, pageSize, pageIndex, order) {
  let res = await get(`${apiURL}/service/${serviceId}/comment?pageSize=${pageSize}&pageIndex=${pageIndex}&order=${order}`)
  for (let item of res.items) item.user = item.commenter
  return res
}

export async function getServiceMessage(serviceId, pageSize, pageIndex, order) {
  let res = await get(`${apiURL}/service/${serviceId}/message?pageSize=${pageSize}&pageIndex=${pageIndex}&order=${order}`)
  for (let item of res.items) item.user = item.messager
  return res
}

export async function collectService(serviceId) {
  return await put(`${apiURL}/service/${serviceId}/collect`)
}

export async function uncollectService(serviceId) {
  return await del(`${apiURL}/service/${serviceId}/uncollect`)
}

export async function likeComment(commentId) {
  return await put(`${apiURL}/service/comment/${commentId}/like`)
}

export async function unlikeComment(commentId) {
  return await del(`${apiURL}/service/comment/${commentId}/unlike`)
}

export async function likeMessage(messageId) {
  return await put(`${apiURL}/service/message/${messageId}/like`)
}

export async function unlikeMessage(messageId) {
  return await del(`${apiURL}/service/message/${messageId}/unlike`)
}

export async function putMessage(serviceId, content) {
  return await put(`${apiURL}/service/${serviceId}/message`, {content})
}

export async function putComment(serviceId, content, rating) {
  return await put(`${apiURL}/service/${serviceId}/comment`, {content, rating})

}

export async function accessService(serviceId) {
  let res = await put(`${apiURL}/service/${serviceId}/access`)
  return res
}

export async function unaccessService(serviceId) {
  let res = await del(`${apiURL}/service/${serviceId}/unaccess`)
  return res
}

// 发布者获取接取人列表
// /api/service/{serviceId}/select/info
export async function getServiceSelectInfo(serviceId, pageSize, pageIndex) {
  return await get(`${apiURL}/service/${serviceId}/select/info?pageSize=${pageSize}&pageIndex=${pageIndex}`);
}

// 发布者取消已发布的任务
// /api/service/{serviceId}/cancel
export async function cancelService(serviceId) {
  return await del(`${apiURL}/service/${serviceId}/cancel`);
}

// 发布者选择接取人，创建订单
// /api/service/{serviceId}/select/confirm
export async function confirmSelectService(serviceId, userList) {
  return await put(`${apiURL}/service/${serviceId}/select/confirm`, {userList});
}

export async function refuseSelectService(serviceId, userList) {
  return await put(`${apiURL}/service/${serviceId}/select/deny`, {userList});
}

export async function getServiceSuccessPeople(serviceId, pageSize, pageIndex){
  return await get(`${apiURL}/service/${serviceId}/select/success?pageSize=${pageSize}&pageIndex=${pageIndex}`);
}

export async function getServiceRefusePeople(serviceId, pageSize, pageIndex){
  return await get(`${apiURL}/service/${serviceId}/select/fail?pageSize=${pageSize}&pageIndex=${pageIndex}`);
}


//

export async function deleteComment(commentId) {
  return await del(`${apiURL}/service/comment/${commentId}`)
}

export async function deleteMessage(messageId) {
  return await del(`${apiURL}/service/message/${messageId}`)
}