import { moodflow } from './moodflow'
import { alasso } from './alasso'
import { writer } from './writer'

export const applications = [writer, moodflow, alasso]
export const applicationsMap = Object.fromEntries(applications.map((a) => [a.id, a]))
