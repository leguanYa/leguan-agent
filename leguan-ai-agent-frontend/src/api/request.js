import axios from 'axios'
import { API_BASE_URL } from '../config'

const request = axios.create({
  baseURL: API_BASE_URL,
  timeout: 60000,
})

export default request
