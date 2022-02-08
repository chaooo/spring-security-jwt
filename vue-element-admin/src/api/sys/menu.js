import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/sys/menu/list',
    method: 'get',
    params: query
  })
}

export function getMenu(id) {
  return request({
    url: '/sys/menu/' + id,
    method: 'get'
  })
}

export function updateMenu(id, data) {
  return request({
    url: '/sys/menu/update/' + id,
    method: 'post',
    data: data
  })
}

export function createMenu(data) {
  return request({
    url: '/sys/menu/create',
    method: 'post',
    data: data
  })
}

export function deleteMenu(id) {
  return request({
    url: '/sys/menu/delete/' + id,
    method: 'post'
  })
}

export function updateSelective(id, data) {
  return request({
    url: '/sys/menu/update/selective/' + id,
    method: 'post',
    data: data
  })
}

export function sysMenuTree() {
  return request({
    url: '/sys/menu/tree',
    method: 'get'
  })
}
