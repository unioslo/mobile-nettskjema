const getFormInfo = async (formId) => {
  await fetch("https://nettskjema.uio.no/ping.html")
  const url = `https://nettskjema.uio.no/answer/answer.json?formId=${formId}`
  const result = await fetch(url)
  const data = await result.json()
  return data
}

export default getFormInfo
