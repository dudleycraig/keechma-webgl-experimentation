/** containers/App.js **/

import React from 'react'
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import Home from './Home'
import NotFound from './NotFound'

export default (props) => {
  return (
    <Router {...props}>
      <Switch>
        <Route exact path="/" component={Home} />
        <Route path="/home" component={Home} />
        <Route component={NotFound} />
      </Switch>
    </Router>
  )
}
