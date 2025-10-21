import errorImg from '../assets/images/error.jpg'

export default function ErrorMessage({ type }) {

  let errorMsg = null

  switch (type) {
    case "network":
      errorMsg = "No internet connection. Please check and try again.";
      break;
    // case "timeout":
    //   errorMsg = "Server is taking too long to respond. Please try again later.";
    //   break
    case "server":
      errorMsg = "Unable to fetch data at the moment. Please try again later.";
      break
    case 'unauthorized':
      errorMsg = 'Access denied'
      break
    case 'not found':
      errorMsg = "This item doesn't exist"
      break
    default:
      errorMsg = "Something went wrong. Please try again.";
      break
  }

  return (
    <div className='flex justify-evenly items-center flex-col max-w-full p-8 text-center text-2xl m-auto'>
      <img className='max-w-60' src={errorImg} alt="error" />
      <h1 className='text-xl'>{errorMsg}</h1>
    </div>
  )
}