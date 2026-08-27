import { moodflow } from './moodflow'
import { alasso } from './alasso'

export const applications = [moodflow, alasso]
export const applicationsMap = Object.fromEntries(applications.map((a) => [a.id, a]))
