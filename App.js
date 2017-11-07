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

import { StackNavigator, } from 'react-navigation';
// if you want to add more react native screens by importing them directly

class Questionnaire extends React.Component {
  render() {
    return(

    );
  }  
}

export default class App extends Component<{}> {
  render() {
    return (
      <View style={{flex: 1, padding: 60, backgroundColor: 'grey'}}>
	<Text style={styles.welcome}>Welcome to A4DN</Text>
        <Text style={{textAlign:'center'}}>The alarm system based around you!</Text>
	<Button onPress={this._moveToTest} title="Get Started"/>
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

const Navi = StackNavigator({
  Home: {screen: App},
  Question: {screen: Questionnaire},
});


