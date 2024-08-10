import {apiURL, get, post} from "./util";

const chatURL = `${process.env.REACT_APP_WS_URL}/chat`

export default function connectWebSocket(userId) {
  const socket = new WebSocket(chatURL)
  socket.onopen = () => {
    socket.send(JSON.stringify({type: 'register', userId: userId}))
    console.log(`WebSocket connection opened, userId: ${userId}`)
  }
  socket.onclose = () => {
    console.log(`WebSocket connection closed, userId: ${userId}`)
  }
  return {
    send: message => socket.send(JSON.stringify(message)),
    close: () => socket.close(),
    set onmessage(onmessage) {
      socket.onmessage = event => {
        console.log(JSON.parse(event.data))
        onmessage(JSON.parse(event.data))
      }
    }
  }
}

export async function getChatList() {
  return await get(`${apiURL}/chat/list`)
}

export async function getChatHistory(chatId, lastMessageId, count) {
  return await get(`${apiURL}/chat/history?chatId=${chatId}&lastMessageId=${lastMessageId}&count=${count}`)
}

export async function enterChat(type, ofId) {
  return await post(`${apiURL}/chat/enter/${type}/${ofId}`)
}