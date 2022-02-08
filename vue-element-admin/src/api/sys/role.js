import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/sys/role/list',
    method: 'get',
    params: query
  })
}

export function getRole(id) {
  return request({
    url: '/sys/role/' + id,
    method: 'get'
  })
}

export function updateRole(id, data) {
  return request({
    url: '/sys/role/update/' + id,
    method: 'post',
    data: data
  })
}

export function createRole(data) {
  return request({
    url: '/sys/role/create',
    method: 'post',
    data: data
  })
}

export function deleteRole(id) {
  return request({
    url: '/sys/role/delete/' + id,
    method: 'post'
  })
}
