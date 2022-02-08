import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/sys/user/info',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/sys/user/logout',
    method: 'post'
  })
}

export function fetchList(query) {
  return request({
    url: '/sys/user/list',
    method: 'get',
    params: query
  })
}

export function getUser(id) {
  return request({
    url: '/sys/user/' + id,
    method: 'get'
  })
}

export function updateUser(id, data) {
  return request({
    url: '/sys/user/update/' + id,
    method: 'post',
    data: data
  })
}

export function createUser(data) {
  return request({
    url: '/sys/user/create',
    method: 'post',
    data: data
  })
}

export function deleteUser(id) {
  return request({
    url: '/sys/user/delete/' + id,
    method: 'post'
  })
}

export function updateProfile(data) {
  return request({
    url: '/sys/user/update/profile',
    method: 'post',
    data: data
  })
}

export function updatePassword(data) {
  return request({
    url: '/sys/user/update/password',
    method: 'post',
    data: data
  })
}
