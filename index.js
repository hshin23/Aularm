import { AppRegistry } from 'react-native';
import React, { Component } from 'react';

import {
  Button,
  Platform,
  StyleSheet,
  Text,
  TextInput,
  View
} from 'react-native';

import { StackNavigator, } from 'react-navigation';

class Questionnaire extends React.Component {
  constructor(props) {
    super(props);
    this.state = inputText;
  }
  render() {
    return(
      <View style={{flex: 1, padding: 60, backgroundColor: 'grey'}}>
        <Text style={styles.welcome}>A few quick questions...</Text> 
          <TextInput style={styles.questions} value={this.state.text}/>
        <Text style={styles.questions}>When do you usually go to sleep?</Text>
          <TextInput style={styles.questions} value={this.state.text_two}/>
        <Text style={styles.questions}>When do you usually wake up?</Text> 
        <Text style={styles.questions}>When are you a heavy sleeper?</Text>
      </View>
    );
  }
}

export default class App extends Component<{}> {
  //This is for determining what path we go to

  render() {
    return (
      <View style={{flex: 1, padding: 60, backgroundColor: 'grey'}}>
        <Text style={styles.welcome}>Welcome to A4DN</Text>
        <Text style={{textAlign:'center'}}>The alarm system based around you!</Text>
        <Button
          onPress={() => this.props.navigation.navigate("Quest",{screen: "Questionnaire"})}
          title="Get Started"/>
      </View>
    );
  }
}

inputText = {
  text: '',
  text_two: '',
}

const styles = StyleSheet.create({
  welcome: {
    fontSize: 40,
    textAlign: 'center',
    margin: 20,
  },
  questions: {
    fontSize: 20,
    textAlign: 'center',
    margin: 20,
  }
});

const Navi = StackNavigator({
  Home: { screen: App },
  Quest: { screen: Questionnaire },
});

AppRegistry.registerComponent('A4DN', () => Navi);
