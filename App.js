/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Button,
  Platform,
  StyleSheet,
  Text,
  View
} from 'react-native';

import { StackRouter, } from 'react-navigation';
// if you want to add more react native screens by importing them directly


// This represents the possible routes that we could take
const route = {
  //Defines Navigation state in response to an action
  getStateForAction: (action,state) => ({
    index: 1,
    routes: [
      {
        routeName: 'startRoute',
        key: 'start',
      },
      {
        routeName: 'questionRoute',
        key: 'question',
      },
      {
        routeName: 'clockRoute',
        key: 'clock',
      },
    ]
  }),
  //Returns the child component or navigator for the given route name
  getActionForPathAndParams: (path, params) => null,
  //Returns the active component for a deep navigation state
  getPathAndParamsForState: (state) => null,
  //Returns an optional navigation action that should be used when
  //The user navigates to this path and provides optional query params
  getComponentForState: (state) => Navi,
  //Returns the path and params that can be put into the URL to link the user
  //back to the same spot
  getComponentForRouteName:(routeName) => Navi,
};

class NaviGuide extends React.Component {

/*  static Navi = StackRouter({
    Home: {screen: App},
    Question: {screen: Questionnaire},
    }, {
    initialRouteName: 'Home',
  });*/
  // "Navi" hosts the route list, so that way we can work on it
  static Navi = route; 
  //static defaultGetStateForAction = Navi.router.getStateForAction;
}

//This is going to represent the next screen that we move to
class Questionnaire extends React.Component {
  render() {
    return(
      <Text>Ye</Text>
    );
  }  
}

export default class App extends Component<{}> {
  //This is for determining what path we go to

  moveTo() {
    
  }

  render() {
    return (
      <View style={{flex: 1, padding: 60, backgroundColor: 'grey'}}>
	<Text style={styles.welcome}>Welcome to A4DN</Text>
        <Text style={{textAlign:'center'}}>The alarm system based around you!</Text>
	<Button onPress={this._moveTo} title="Get Started"/>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  welcome: {
    fontSize: 40,
    textAlign: 'center',
    margin: 20,
  }
});
